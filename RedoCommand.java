public class RedoCommand extends CommandManager{

    public RedoCommand(Receiver receiver) {
        super(receiver);
    }
    @Override
    public String execute() {
        return this.receiver.redo();
    }
}
