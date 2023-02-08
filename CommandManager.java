public abstract class CommandManager {
    Receiver receiver;

    public CommandManager(Receiver receiver) {
        this.receiver = receiver;
    }

    public abstract String execute();
}
