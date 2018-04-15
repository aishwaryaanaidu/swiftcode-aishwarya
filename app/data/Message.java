package data;

public class Message {
    public FeedResponse feedResponse;
    public enum Sender {USER,BOT};
    public String text;
    public Sender sender;

}
