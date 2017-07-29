# HOW TO USE BEL
This manual is introduce you to BEL functionality.

### Access to included *boors.

Each *boor in library - is singleton, which contain basic information about it. 
```
Danbooru.get();
```

Now we get access to Danbooru and can create simple request. Let's try it
```
Danbooru.get().getPostByIdRequest(2800847);
```
Result will be next: https://danbooru.donmai.us/posts/2800847.json.

### *boor Structure
Each included *boor in BEL has some basic methods. 
2 of them - ```getApi()``` and ```getFormat()```.

```getApi()``` is return boor API enum. 
It can be ```BASICS```(Gelbooru), ```ADVANCED```(Danbooru) or ```UNDEFINED```.

```getFormat()``` is return boor result format. It can be ```XML``` or ```JSON```. 
And, of course there are some dependencies between ```API``` and ```FORMAT```.
If *boor has ```API.BASIC``` the format will be ```FORMAT.XML``` always. 
But, if *boor has ```API.ADVANCED``` the format can be ```FORMAT.XML``` or ```FORMAT.JSON```.

When you call method for creating request, you can choose result format.
```
Yandere.get().getPackByTagsRequest(count, "tags", pageId, Format.JSON);
```

### XML Result

Before this we just playing in the local machine. 
And now we try to get a first result.

At first we get *boor and create a simple request
```
String request = Safebooru.get().getPostByIdRequest(2285559);
```
After this let's create connection to the *boor
```
HttpConnection connection = new HttpConnection(false);
```
And get response data from server
```
String response = connection.getRequest(request);
```
Now the ```response``` will be contain XML or JSON document

Full code:
```
String request = Safebooru.get().getPostByIdRequest(2285559);
HttpConnection connection = new HttpConnection(false);
String response = connection.getRequest(request);
```
### Parse Data
After getting result from server we must somehow process the data.
In BEL there are two classes for it - ```XmlParser``` and 
```JsonParser```. Let's try one of them
```
XmlParser parser = new XmlParser();
```
The XmlParser is not supported raw data, so we create a stream based on it.
```
InputStream is = new ByteArrayInputStream(response.getBytes());
parser.startParse(is);
```
When the parse end - get the result by method.
```
List<HashMap<String, String>> result = parser.getResult();
```
As result we receive the ```List<HashMap<String, String>>``` - 
this is the list of items(```Post```, ```Comment```), where the ```HashMap``` is a single item.
Item(```HashMap```) has next structure - ```<Attribute_name, Attrubute_value>```.

We can simplify this code. Look at it
```
//create parser
XmlParser parser = new XmlParser();   
//start parser - we can put url as argument
parser.startParse(Safebooru.get().getPostByIdRequest(2285559));
//get profit
List<HashMap<String, String>> result = parser.getResult();
```

Now, let's try JsonParser. 
```
HttpConnection connection = new HttpConnection();
String response = connection.getRequest(Danbooru.get().getPostByIdRequest(2794154));
JsonParser parser = new JsonParser();
parser.startParse(response);
List<HashMap<String, String>> result = parser.getResult();
```
```JsonParser.startParse``` is support only raw data.

### Post and Comment
After all, we must create easiest way to manipulate data. So we create 
```Post``` or ```Comment```. We put in their constructors 
```HashMap<String, String>```.

```
Post post = new Post(result, Danbooru.get());
```
The second arg is the *boor instance.

Same for Comment:
```
Comment comment = new Comment(hashmap);
```
Comment is not needed boor instance.

You can create remote Post constructor in boor, implements 
`newPostInstance(HashMap)`. This method can create Post 
more flexibly.

If `Post` or `Comment` functionality is not satisfies your needs - 
you can extends them.

### Adding your own *boor.
Now, let's add new boor. I choose [TBIB](https://tbib.org/index.php?page=help&topic=dapi).
It has Basic API, so, we can extend `AbstractBoorBasic` or `AbstractBoor`.
Let's extend `AbstractBoor`.

At first create new class-singleton and adding required methods
```
public class BigBooru extends AbstractBoor {

    private static BigBooru instance = null;
        
    public static BigBooru get(){
        if (instance == null) instance = new BigBooru();
        return instance;
    }

    @Override
    public Format getFormat() {
        return null;
    }

    @Override
    public Api getApi() {
        return null;
    }

    @Override
    public String getCustomRequest(String request) {
        return null;
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return null;
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format format) {
        return null;
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return null;
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes) {
        return null;
    }
}
```
At first we must add 2 fields - Api and Format and implement some methods.
```
public class BigBooru extends AbstractBoor {
    // /~
    //final because API is basic and we can't get result in JSON format
    private final Format format = Format.XML; 
    
    private final Api api = Api.BASICS;
    
    @Override
    public Format getFormat() { return format; }

    @Override
    public Api getApi() { return api; }    
    
    // /~
}
```
Now add API access and some more methods
```
    @Override
    public String getCustomRequest(String request) {
        return "https://tbib.org/index.php?page=dapi&q=index&s=";
    }

    @Override
    public String getPostByIdRequest(int id, Format ignore) {
        return getCustomRequest("s=post&id=" + id);
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format ignore) {
        return getCustomRequest("post&limit=" + limit + "&tags=" + tags + "&pid=" + page);
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("comment&post_id=" + post_id);
    }
```
And in the finish implement `newPostInstance` method - we must know post structure.
```
        @Override
        public Post newPostInstance(HashMap<String, String> attributes) {
            Post post = new Post(instance);
            //create Entry
            Set<Map.Entry<String, String>> entrySet = attributes.entrySet();
            //for each attribute
            for (Map.Entry<String, String> pair : entrySet) {
                switch (pair.getKey()){
                    case "id":{
                        post.setId(Integer.parseInt(pair.getValue()));
                        break;
                    }
                    case "md5":{
                        post.setMd5(pair.getValue());
                        break;
                    }
                    case "rating":{
                        post.setRating(pair.getValue());
                        break;
                    }
                    case "score":{
                        post.setScore(Integer.parseInt(pair.getValue()));
                        break;
                    }
                    case "preview_url":{
                        post.setPreview_url("https:" + pair.getValue());
                        break;
                    }
                    case "tags":{
                        post.setTags(pair.getValue());
                        break;
                    }
                    case "sample_url":{
                        post.setSample_url("https:" + pair.getValue());
                        break;
                    }
                    case "file_url":{
                        post.setFile_url("https:" + pair.getValue());
                        break;
                    }
                    case "source":{
                        post.setSource(pair.getValue());
                        break;
                    }
                    case "creator_id": {
                        post.setCreator_id(Integer.parseInt(pair.getValue()));
                        break;
                    }
                    case "has_comments": {
                        if ("true".equals(pair.getValue())) {
                            post.setHas_comments(true);
                        } else {
                            post.setHas_comments(false);
                        }
                        break;
                    }
                    case "created_at":{
                        post.setCreate_time(pair.getValue());
                        break;
                    }
                }
            }
            //after all check comments flag
            if (post.isHas_comments()){
                //and if true - setup comments url.
                post.setComments_url(instance.getCommentsByPostIdRequest(post.getId()));
            }
            return post;
        }
```
