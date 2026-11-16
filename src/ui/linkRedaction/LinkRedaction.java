package ui.linkRedaction;

public abstract class LinkRedaction {
    public enum Status {
        DELETE, EDIT
    }

    private final Status status;
    private final int limit;

    public LinkRedaction(Status status, int limit) {
        this.status = status;
        this.limit = limit;
    }

    public Status getStatus() {
        return status;
    }

    public int getLimit() {
        return limit;
    }
}
