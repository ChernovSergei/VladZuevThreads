package messageTransport.producer;

import messageTransport.model.Message;

public class MessageFactory {
        private static final int INITIAL_INDEX = 1;
        private static final String TEMPLATE_MSG_DATA = "Message#%d";
        private int nextMessage;

        public MessageFactory() {
            this.nextMessage = INITIAL_INDEX;
        }

        public Message create() {
            return new Message(String.format(TEMPLATE_MSG_DATA, this.findAndIncrementNextMSGIndex()));
        }

        private synchronized int findAndIncrementNextMSGIndex() {
            return this.nextMessage++;
        }
}
