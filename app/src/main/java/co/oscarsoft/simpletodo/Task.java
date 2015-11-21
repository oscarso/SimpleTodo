package co.oscarsoft.simpletodo;

/**
 * Created by osso on 11/20/15.
 */
public class Task {
    private String taskName;

    public Task(String taskName) {
        this.setTaskName(taskName);
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }
    
}
