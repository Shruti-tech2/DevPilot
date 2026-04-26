package devpilot.devpilot.webhook;

import lombok.Data;
import java.util.List;

@Data
public class GitHubPushEvent {

    private String ref;
    private String before;
    private String after;
    private Boolean created;
    private Boolean deleted;
    private Boolean forced;

    private Repository repository;
    private Pusher pusher;
    private Sender sender;

    private List<Commit> commits;
    private Commit head_commit;

    @Data
    public static class Repository {
        private Long id;
        private String name;
        private String full_name;
        private String html_url;
    }

    @Data
    public static class Pusher {
        private String name;
        private String email;
    }

    @Data
    public static class Sender {
        private String login;
        private Long id;
    }

    @Data
    public static class Commit {
        private String id;
        private String message;
        private String timestamp;
        private String url;
        private Author author;

        private List<String> added;
        private List<String> removed;
        private List<String> modified;
    }

    @Data
    public static class Author {
        private String name;
        private String email;
    }
}