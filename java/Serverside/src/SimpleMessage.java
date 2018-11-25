import org.json.simple.JSONObject;

public class SimpleMessage {

	int type;
	/* 0 --> login/logout
	 * 1 --> add message to conversation
	 * 2 --> add user to conversation
	 * 3 --> start new conversation
	 */
	
	String message;
	String conversation;
	String extra;
	
	public SimpleMessage(int type, String conversation, String message, String extra) {
		
		this.type=type;
		this.conversation = conversation;
		this.message = message;
		this.extra =extra;
		
	}
	
	//create from JSON
	public SimpleMessage(JSONObject jo) {
		
		type = (Integer) jo.get("type");
		conversation = (String) jo.get("conversation");
		message = (String) jo.get("message");
		extra = (String) jo.get("extra");
		
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getConversation() {
		return conversation;
	}
	
	//convert to JSON
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();

	      obj.put("type", new Integer(type));
	      obj.put("conversation", conversation);
	      obj.put("message", message);
	      obj.put("extra", extra);
	      
	      return obj;
		
	}

	public String getExtra() {
		// TODO Auto-generated method stub
		return extra;
	}
	
	
}
