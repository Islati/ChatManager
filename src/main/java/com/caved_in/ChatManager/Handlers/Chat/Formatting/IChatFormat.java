package com.caved_in.chatmanager.handlers.chat.formatting;

public interface IChatFormat
{
	/**
	 * Get the formats identifier
	 * @return
	 */
    public String getName();

	/**
	 * Get the chat format in which messages should be displayed
	 * @return
	 */
    public String getFormat();

	/**
	 * Get a message formatted with the defined format, "sent" by the
	 * Sender parameter
	 * @param messageSender
	 * @param message
	 * @return
	 */
    public String getFormattedMessage(String messageSender, String message);

	/**
	 * Set the channel format
	 * @param Format
	 */
    public void setFormat(String Format);
}
