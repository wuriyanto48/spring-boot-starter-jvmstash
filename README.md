## Spring Boot Starter JVM Stash (Logstash Client Library for Spring Boot)

https://github.com/wuriyanto48/jvm-stash for Spring Boot.

#### Usage

Add this config to your `application.properties`
```editorconfig
stash.host=localhost
stash.port=5000
stash.secure=true
stash.keyStorePath=server.p12
stash.keyStorePassword=damn12345
```

Just `Autowired` the `StashTemplate` and now ready to go
```java
@RestController
public class IndexController {

    private final static Logger LOGGER = Logger.getLogger(IndexController.class.getName());

    @Autowired
    private StashTemplate stashTemplate;

    @GetMapping("/")
    public String index(HttpServletRequest request) {

        LOGGER.addHandler(new StashLogHandler(stashTemplate.getStash()));

        LOGGER.log(Level.INFO, request.getMethod());

        return "hello";
    }
}
```

Example Result Data
```json
{
        "_index" : "activity_log",
        "_type" : "_doc",
        "_id" : "n1cYAHIBVwCwW5urEtlM",
        "_score" : 1.0,
        "_source" : {
          "@version" : "1",
          "@timestamp" : "2020-05-10T19:37:56.956Z",
          "port" : 60958,
          "host" : "gateway",
          "message" : {
            "sourceClassName" : "com.wuriyanto.application.IndexController",
            "sequenceNumber" : 13,
            "instant" : {
              "nano" : 906778000,
              "epochSecond" : 1589139476
            },
            "message" : "GET",
            "resourceBundleName" : null,
            "parameters" : null,
            "resourceBundle" : null,
            "threadID" : 28,
            "sourceMethodName" : "index",
            "level" : {
              "name" : "INFO",
              "localizedName" : "INFO",
              "resourceBundleName" : "sun.util.logging.resources.logging"
            },
            "loggerName" : "com.wuriyanto.application.IndexController",
            "thrown" : null,
            "millis" : 1589139476906
          }
        }
      }
```