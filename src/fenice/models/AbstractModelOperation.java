package fenice.models;

public class AbstractModelOperation {
	
	public DBOperationAdapter dbInstance = null;
	
	public AbstractModelOperation() {
		dbInstance = DBOperationAdapter.GetInstance();
	}
	
	
}
