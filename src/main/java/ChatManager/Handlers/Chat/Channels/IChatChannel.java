package ChatManager.Handlers.Chat.Channels;

import java.util.List;

public interface IChatChannel
{
    public String getName();
    public String getPrefix();
    public void setName(String Name);
    public void setPrefix(String Name);
    public List<String> getMembers();
    public void addMember(String Name);
    public void removeMember(String Name);
    public boolean isMember(String Name);
    public boolean allowJoinLeaveMessages();
    public String getPermission();
    public boolean hasPermission();
    public boolean isPrivate();
    public void setPermission(String Permission);
    public void setJoinLeaveMessages(boolean Messages);
    public void setPrivate(boolean Private);
    public void sendToMembers(String Sender, String Message);
    public String getCreator();
    public void setCreator(String Creator);
    public boolean isPermanant();
    public void setPermanant(boolean Permanant);
}
