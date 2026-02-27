import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class edit extends JFrame {
    private JTextField nameField, ageField;
    private JTable recordTable;
    private DefaultTableModel tableModel;

    // 相对路径：从 edit.class 所在目录出发，找到 settings&data 目录
    private final String propPath = "settings&data/settings.properties";
    private final String ledgerPath = "settings&data/ledger.csv";

    public edit() {
        setTitle("编辑用户信息 & 记账");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 确保数据目录存在（用相对路径创建）
        File dataDir = new File("settings&data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // 确保账本文件存在
        File ledgerFile = new File(ledgerPath);
        if (!ledgerFile.exists()) {
            try {
                ledgerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JTabbedPane tabbedPane = new JTabbedPane();

        // ===== Tab1: 基本信息 =====
        JPanel basicPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        basicPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        basicPanel.add(new JLabel("名字："));
        nameField = new JTextField();
        basicPanel.add(nameField);
        basicPanel.add(new JLabel("年龄："));
        ageField = new JTextField();
        basicPanel.add(ageField);
        JButton saveBasicBtn = new JButton("保存基本信息");
        basicPanel.add(saveBasicBtn);
        JButton clearBtn = new JButton("清空输入数据");
        clearBtn.addActionListener(e -> loadProps());
        basicPanel.add(clearBtn);
        tabbedPane.add("基本信息", basicPanel);

        // ===== Tab2: 记账 =====
        JPanel ledgerPanel = new JPanel(new BorderLayout(10, 10));
        ledgerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new FlowLayout());
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"收入", "支出"});
        JTextField amountField = new JTextField(10);
        JTextField noteField = new JTextField(15);
        JButton addBtn = new JButton("添加记录");
        inputPanel.add(new JLabel("类型："));
        inputPanel.add(typeCombo);
        inputPanel.add(new JLabel("金额："));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("备注："));
        inputPanel.add(noteField);
        inputPanel.add(addBtn);

        tableModel = new DefaultTableModel(new String[]{"日期", "类型", "金额", "备注"}, 0);
        recordTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(recordTable);

        ledgerPanel.add(inputPanel, BorderLayout.NORTH);
        ledgerPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.add("记账", ledgerPanel);

        add(tabbedPane);

        // 初始化：加载已有的 name 和 age（不管 is_active）
        loadProps();
        loadRecords();

        // 事件
        saveBasicBtn.addActionListener(e -> saveProps());
        addBtn.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String amountStr = amountField.getText().trim();
            String note = noteField.getText().trim();
            if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "金额不能为空");
                return;
            }
            try {
                double amount = Double.parseDouble(amountStr);
                addRecord(type, amount, note);
                amountField.setText("");
                noteField.setText("");
                loadRecords();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "金额必须是数字");
            }
        });

        setVisible(true);
    }

    /**
     * 加载 settings.properties 中的 name 和 age（不检查 is_active）
     */
    private void loadProps() {
        nameField.setText("");
        ageField.setText("");

        File file = new File(propPath);
        if (!file.exists()) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(fis);

            // 直接读取 name 和 age，不检查 is_active
            nameField.setText(props.getProperty("name", ""));
            ageField.setText(props.getProperty("age", "0"));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "读取配置出错：" + ex.getMessage());
        }
    }

    /**
     * 保存 name 和 age 到 settings.properties（不写 is_active）
     */
    private void saveProps() {
        String name = nameField.getText().trim();
        String ageStr = ageField.getText().trim();
        if (name.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "名字和年龄不能为空");
            return;
        }
        try {
            int age = Integer.parseInt(ageStr);
            File file = new File(propPath);
            // 用 BufferedWriter 自己写，无日期注释
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("name=" + name);
                bw.newLine();
                bw.write("age=" + age);
                bw.newLine();
                bw.write("is_active=true"); // 始终写入 is_active=true，保持激活状态
            }
            JOptionPane.showMessageDialog(this, "基本信息保存成功！");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存失败：" + ex.getMessage());
        }
    }

    /**
     * 加载账本记录（不检查 is_active，因为 loadProps 已不检查）
     */
    private void loadRecords() {
        tableModel.setRowCount(0);

        File file = new File(ledgerPath);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 添加一条账本记录
     */
    private void addRecord(String type, double amount, String note) {
        String date = java.time.LocalDate.now().toString();
        String line = date + "," + type + "," + amount + "," + note;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ledgerPath, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(edit::new);
    }
}