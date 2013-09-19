package ChatManager.Handlers.Chat.Formatting;

public interface IChatFormat
{
    public String getName();
    public String getFormat();
    public String getFormattedMessage(String Sender, String Message);
    public void setFormat(String Format);
}
