public class UndoCommand extends CommandManager{

    public UndoCommand(Receiver receiver) {
        super(receiver);
    }
    @Override
    public String execute() {
        return this.receiver.undo();
    }
}
