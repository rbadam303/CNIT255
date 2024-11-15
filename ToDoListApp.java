import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ToDoListApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}

// 1. GUI Class creates main interface
class GUI {
    JFrame frame;
    JPanel mainPanel;
    JPanel taskPanel;
    JTextField taskField;
    JButton addButton;
    ArrayList<Task> tasks;
    String listName;

    public GUI() {

        listName = JOptionPane.showInputDialog("Enter a name for your To-Do List:");

        // Blank entry case
        if (listName == null || listName.trim().isEmpty()) {
            listName = "To-Do List";
        }

        frame = new JFrame(listName);
        mainPanel = new JPanel();
        taskPanel = new JPanel();
        tasks = new ArrayList<>();


        JLabel titleLabel = new JLabel(listName);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel);


        taskField = new JTextField(15);  
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        taskField.setBorder(border);

        addButton = new JButton("Add Task");
        addButton.addActionListener(new AddTaskListener());

        JPanel inputPanel = new JPanel();
        inputPanel.add(taskField);
        inputPanel.add(addButton);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);


        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);  
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(380, 300));  
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Listener Setup to add new tasks
    class AddTaskListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String taskName = taskField.getText();
            if (!taskName.isEmpty()) {
                Task newTask = new Task(taskName);
                tasks.add(newTask);
                TaskUI taskUI = new TaskUI(newTask);
                taskUI.setAlignmentX(Component.LEFT_ALIGNMENT);  
                taskPanel.add(taskUI);
                taskPanel.revalidate();
                taskPanel.repaint();
                taskField.setText("");
            }
        }
    }
}

// 2. User Class holds user info
class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// 3. Task Class allows for task information
class Task {
    private String taskName;
    private boolean isDone;

    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}

// 4. Notes Class note field creation
class Notes {
    private String noteContent;

    public Notes(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
}

// 5. TaskUI Class the delete button and checkmark
class TaskUI extends JPanel {
    private Task task;
    private JCheckBox doneCheckBox;
    private JButton deleteButton;
    private JTextField noteField;

    public TaskUI(Task task) {
        this.task = task;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(new EmptyBorder(2, 5, 2, 5));  

        doneCheckBox = new JCheckBox(task.getTaskName());
        deleteButton = new JButton("Delete");

        noteField = new JTextField(10) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 20);
            }
        };

        doneCheckBox.addActionListener(new DoneListener());
        deleteButton.addActionListener(new DeleteListener());

        add(doneCheckBox);
        add(deleteButton);
        add(noteField);
    }


    class DoneListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            task.setDone(doneCheckBox.isSelected());
        }
    }


    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Container parent = TaskUI.this.getParent();
            parent.remove(TaskUI.this);
            parent.revalidate();
            parent.repaint();
        }
    }
}
