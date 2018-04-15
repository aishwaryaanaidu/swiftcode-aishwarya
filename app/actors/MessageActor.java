package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

import static data.Message.Sender.BOT;
import static data.Message.Sender.USER;

public class MessageActor extends UntypedActor {
    //self ref the actor
    //PROPS
    //obj of FeedService
    //obj of NewsAgentService
    //define another actor ref
    private final ActorRef out; //rhs
    public ObjectMapper mapper = new ObjectMapper();

    public MessageActor(ActorRef out) {

        this.out = out;
    }

    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
    }

    private FeedService feedService = new FeedService();
    private NewsAgentService newsAgentService = new NewsAgentService();
    public FeedResponse feedResponse = new FeedResponse();
    public NewsAgentResponse newsAgentResponse = new NewsAgentResponse();

    @Override
    public void onReceive(Object message) throws Throwable {
        //Send back the response
        if ((message instanceof String)) {
            Message messageObject = new Message();
            messageObject.text = message.toString();
            messageObject.sender = USER;
            out.tell(mapper.writeValueAsString(messageObject), self());
            String query = newsAgentService.getNewsAgentResponse("Find " + message, UUID.randomUUID()).query;
            feedResponse = feedService.getFeedByQuery(query);
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + query;
            messageObject.feedResponse = feedResponse;

            messageObject.sender = BOT;
            out.tell(mapper.writeValueAsString(messageObject), self());
        }

    }
}