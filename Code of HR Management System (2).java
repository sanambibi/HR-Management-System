import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;
import java.time.*;
import java.util.*;

class Employee {
    String name;
    String id;
    LocalDateTime entryTime;
    LocalDateTime exitTime;

    public Employee(String name, String id) {
        this.name = name;
        this.id = id;
        this.entryTime = null;
        this.exitTime = null;
    }
}

class Interview {
    private String[] questions = {
            " Tell me about yourself.",
            " Why do you want to work here?",
            " What are your strengths?",
            " What are your weaknesses?",
            " Where do you see yourself in 5 years?",
            " How do you handle pressure?",
            " Why should we hire you?",
            " What is your expected salary?",
            " Do you have any experience?",
            " Are you willing to relocate?"
    };

    public void conductInterview() {
        JTextArea questionArea = new JTextArea();//ye aik textbox open kare ga
        questionArea.setEditable(false);//is ka matlab hai k jo words likhay gaye hain wo edit nahi hongay
        questionArea.setLineWrap(true);//agar line lambi ho toh wo khud nextline mai shift kare ga
        questionArea.setWrapStyleWord(true);//yeh words ko pure tariqay se shift kare ga
        StringBuilder sb = new StringBuilder();//aik obj string k liye
        for (int i = 0; i < questions.length; i++) { //jis ke andar har question pai apply hoga
            sb.append((i + 1) + ". " + questions[i] + "\n\n");
        }
        questionArea.setText(sb.toString());//sab question ko question area mai dale ga
        JScrollPane scrollPane = new JScrollPane(questionArea);//ye scrollbar open kare ga
        scrollPane.setPreferredSize(new Dimension(400, 300));//scrollbar ki dimension
        JOptionPane.showMessageDialog(null, scrollPane, "Interview Questions", JOptionPane.INFORMATION_MESSAGE);
    }
}

class SalaryCalculator {
    private static final double HOURLY_RATE = 500;

    public static double calculateSalary(LocalDateTime entry, LocalDateTime exit) {
        long hoursWorked = Duration.between(entry, exit).toHours();
        if (hoursWorked < 0) {
            return 0;
        }
        return hoursWorked * HOURLY_RATE;
    }
}

public class HR_Management_System {
    private static Map<String, Employee> employeeMap = new HashMap<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("HR Management System");
        frame.setSize(450, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 3, 10, 10));
        frame.setLocationRelativeTo(null); // center the frame

        JButton registerBtn = new JButton("Register Employee");
        JButton interviewBtn = new JButton("Conduct Interview");
        JButton entryBtn = new JButton("Mark Entry Time");
        JButton exitBtn = new JButton("Mark Exit Time");
        JButton salaryBtn = new JButton("Calculate Salary");

        // Register Employee
        registerBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter Employee Name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Invalid name entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = JOptionPane.showInputDialog(frame, "Enter Employee ID:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (employeeMap.containsKey(id)) {
                JOptionPane.showMessageDialog(frame, "Employee ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Employee emp = new Employee(name.trim(), id.trim());
            employeeMap.put(id.trim(), emp);
            JOptionPane.showMessageDialog(frame, "Employee Registered Successfully");
        });

        // Conduct Interview
        interviewBtn.addActionListener(e -> {
            new Interview().conductInterview();
        });

        // Mark Entry Time
        entryBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Employee ID:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Employee emp = employeeMap.get(id.trim());
            if (emp != null) {
                emp.entryTime = LocalDateTime.now();
                JOptionPane.showMessageDialog(frame, "Entry Time Marked: " + emp.entryTime.toString());
            } else {
                JOptionPane.showMessageDialog(frame, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Mark Exit Time
        exitBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Employee ID:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Employee emp = employeeMap.get(id.trim());
            if (emp != null) {
                if (emp.entryTime == null) {
                    JOptionPane.showMessageDialog(frame, "Entry time not marked yet for this employee.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                emp.exitTime = LocalDateTime.now();
                if (emp.exitTime.isBefore(emp.entryTime)) {
                    JOptionPane.showMessageDialog(frame, "Exit time cannot be before entry time.", "Error", JOptionPane.ERROR_MESSAGE);
                    emp.exitTime = null;
                    return;
                }
                JOptionPane.showMessageDialog(frame, "Exit Time Marked: " + emp.exitTime.toString());
            } else {
                JOptionPane.showMessageDialog(frame, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Calculate Salary
        salaryBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Employee ID:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Employee emp = employeeMap.get(id.trim());
            if (emp != null) {
                if (emp.entryTime == null || emp.exitTime == null) {
                    JOptionPane.showMessageDialog(frame, "Incomplete entry/exit data for this employee.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double salary = SalaryCalculator.calculateSalary(emp.entryTime, emp.exitTime);
                if (salary <= 0) {
                    JOptionPane.showMessageDialog(frame, "Invalid time data for salary calculation.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(frame, "Total Salary: Rs. " + salary);
            } else {
                JOptionPane.showMessageDialog(frame, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(registerBtn);
        frame.add(interviewBtn);
        frame.add(entryBtn);
        frame.add(exitBtn);
        frame.add(salaryBtn);

        frame.setVisible(true);
   }
}