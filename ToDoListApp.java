import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ToDoListApp {
    public static void main(String[] args) {
        // Apply System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Set custom fonts and colors globally
            UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
            UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 14));
            UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 14));
            UIManager.put("Panel.background", new Color(245, 245, 245));
            UIManager.put("Button.background", new Color(220, 220, 220));
            UIManager.put("Button.focus", new Color(245, 245, 245));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        taskField = new JTextField(20);
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        taskField.setBorder(border);

        addButton = new JButton("Add Task");
        addButton.setBackground(new Color(173, 216, 230));
        addButton.setFocusPainted(false);

        // Change button appearance on hover
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(135, 206, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(173, 216, 230));
            }
        });

        addButton.addActionListener(new AddTaskListener());

        JPanel inputPanel = new JPanel();
        inputPanel.add(taskField);
        inputPanel.add(addButton);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setSize(500, 600);
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
                taskPanel.add(taskUI);
                taskPanel.revalidate();
                taskPanel.repaint();
                taskField.setText("");
            }
        }
    }
}

// 2. User Class holds user info (Not modified as per instruction)
class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// 3. Task Class allows for task information (Not modified as per instruction)
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

// 4. Notes Class note field creation (Not modified as per instruction)
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

    public TaskUI(Task task) {
        this.task = task;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.WHITE);

        // Left panel for checkbox and task name
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        doneCheckBox = new JCheckBox();
        doneCheckBox.setOpaque(false);
        JLabel taskLabel = new JLabel(task.getTaskName());
        taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        doneCheckBox.addActionListener(new DoneListener(taskLabel));
        leftPanel.add(doneCheckBox);
        leftPanel.add(taskLabel);

        // Right panel for delete button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(240, 128, 128));
        deleteButton.setFocusPainted(false);
        deleteButton.setForeground(Color.BLACK);

        // Change button appearance on hover
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(205, 92, 92));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(240, 128, 128));
            }
        });

        deleteButton.addActionListener(new DeleteListener());
        rightPanel.add(deleteButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    class DoneListener implements ActionListener {
        private JLabel taskLabel;

        public DoneListener(JLabel taskLabel) {
            this.taskLabel = taskLabel;
        }

        public void actionPerformed(ActionEvent e) {
            task.setDone(doneCheckBox.isSelected());
            if (task.isDone()) {
                taskLabel.setFont(taskLabel.getFont().deriveFont(Font.ITALIC));
                taskLabel.setForeground(Color.GRAY);
            } else {
                taskLabel.setFont(taskLabel.getFont().deriveFont(Font.PLAIN));
                taskLabel.setForeground(Color.BLACK);
            }
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

