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

    //self reference the Actor
    //props
    //object of feed service
    //object of NewsAgentService
    //define another actor Reference
    public MessageActor(ActorRef out) {
        this.out = out;
    }

    private final ActorRef out;

    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
    }

    private FeedService feedService = new FeedService();
    private NewsAgentService newsAgentService = new NewsAgentService();
    public FeedResponse feedResponse = new FeedResponse();
    public ObjectMapper mapper=new ObjectMapper();
    NewsAgentResponse newsAgentResponse=new NewsAgentResponse();
    @Override
    public void onReceive(Object message) throws Throwable {
        //send back the response

        if (message instanceof String) {
            Message messageobject = new Message();
            messageobject.text=message.toString();
            messageobject.sender=USER;
            out.tell(mapper.writeValueAsString(messageobject), self());
            newsAgentResponse=newsAgentService.getNewsAgentResponse(messageobject.text,UUID.randomUUID());
            feedResponse=feedService.getFeedByQuery(newsAgentResponse.query);
            messageobject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + newsAgentResponse.query;
            messageobject.feedResponse=feedResponse;

            messageobject.sender =BOT;
            out.tell(mapper.writeValueAsString(messageobject), self());

        }
    }
}