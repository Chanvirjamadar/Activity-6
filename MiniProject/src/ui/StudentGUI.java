package ui;

import dao.FileHandler;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Student;
import model.ValidationException;
import service.StudentManager;

/**
 * Main GUI Desktop Application for Student Management System.
 * Developed strictly in Java Swing following OOP and modern UI design principles.
 */
public class StudentGUI extends JFrame {

    private final StudentManager studentManager;

    // Card Layout for main content switching
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentArea = new JPanel(cardLayout);

    // Sidebar navigation buttons
    private ModernButton btnNavDashboard;
    private ModernButton btnNavAdd;
    private ModernButton btnNavView;
    private ModernButton btnNavUpdate;
    private ModernButton btnNavDelete;
    private ModernButton btnNavSearch;
    private ModernButton btnNavAnalytics;

    // Badges & Labels
    private JLabel lblSidebarTotalCount;
    private JLabel lblStatusBarMessage;
    private JLabel lblStatusBarPath;

    // Dashboard Components
    private JLabel lblStatTotal;
    private JLabel lblStatDepts;
    private JLabel lblStatMale;
    private JLabel lblStatFemale;
    private DefaultTableModel recentTableModel;

    // Add Student Form Components
    private JTextField txtAddId;
    private JTextField txtAddName;
    private JTextField txtAddRoll;
    private JComboBox<String> cmbAddDept;
    private JComboBox<String> cmbAddYear;
    private JTextField txtAddEmail;
    private JTextField txtAddMobile;
    private JComboBox<String> cmbAddGender;

    // View All Students Components
    private ModernTable viewTable;
    private DefaultTableModel viewTableModel;
    private JComboBox<String> cmbViewFilterDept;
    private JComboBox<String> cmbViewFilterYear;
    private JTextField txtViewSearch;
    private JLabel lblViewRecordCount;

    // Update Student Components
    private JTextField txtUpdateSearchId;
    private JTextField txtUpdateId;
    private JTextField txtUpdateName;
    private JTextField txtUpdateRoll;
    private JComboBox<String> cmbUpdateDept;
    private JComboBox<String> cmbUpdateYear;
    private JTextField txtUpdateEmail;
    private JTextField txtUpdateMobile;
    private JComboBox<String> cmbUpdateGender;
    private ModernButton btnUpdateSave;

    // Delete Student Components
    private JTextField txtDeleteSearchId;
    private JLabel lblDeleteInfoId;
    private JLabel lblDeleteInfoName;
    private JLabel lblDeleteInfoRoll;
    private JLabel lblDeleteInfoDept;
    private JLabel lblDeleteInfoEmail;
    private JLabel lblDeleteInfoMobile;
    private JPanel pnlDeleteStudentCard;
    private ModernButton btnDeleteConfirm;
    private String currentStudentIdToDelete = null;

    // Search Student Components
    private JTextField txtSearchQuery;
    private JComboBox<String> cmbSearchCriteria;
    private ModernTable searchTable;
    private DefaultTableModel searchTableModel;
    private JLabel lblSearchStatus;

    // Analytics Components
    private DefaultTableModel analyticsTableModel;
    private JLabel lblAnalyticsMale;
    private JLabel lblAnalyticsFemale;
    private JLabel lblAnalyticsOther;

    // Standard Department & Year Options
    private static final String[] DEPARTMENTS = {
            "Computer Science",
            "Information Technology",
            "Electronics & Comm",
            "Mechanical Engineering",
            "Civil Engineering",
            "Electrical Engineering",
            "AI & Data Science"
    };

    private static final String[] YEARS_SEMESTERS = {
            "1st Year (Sem 1)", "1st Year (Sem 2)",
            "2nd Year (Sem 3)", "2nd Year (Sem 4)",
            "3rd Year (Sem 5)", "3rd Year (Sem 6)",
            "4th Year (Sem 7)", "4th Year (Sem 8)"
    };

    private static final String[] GENDERS = {"Male", "Female", "Other"};

    private static final String[] TABLE_COLUMNS = {
            "Student ID", "Full Name", "Roll Number", "Department",
            "Year / Semester", "Email Address", "Mobile Number", "Gender"
    };

    /**
     * Constructs and initializes the Student Management System GUI.
     */
    public StudentGUI() {
        this.studentManager = new StudentManager(new FileHandler());
        initComponents();
        refreshAllDataViews();
    }

    /**
     * Initializes the JFrame and GUI components.
     */
    private void initComponents() {
        setTitle("Student Management System - College Administrator Portal");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);

        // Window Closing Confirmation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmAndExit();
            }
        });

        // Main layout container
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UITheme.COLOR_BACKGROUND);

        // Build Panels
        JPanel sidebar = createSidebarPanel();
        JPanel centerContainer = createCenterContainer();
        JPanel statusBar = createStatusBar();

        rootPanel.add(sidebar, BorderLayout.WEST);
        rootPanel.add(centerContainer, BorderLayout.CENTER);
        rootPanel.add(statusBar, BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    // ==========================================
    // Sidebar Navigation Panel
    // ==========================================

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(UITheme.COLOR_SIDEBAR_BG);
        sidebar.setBorder(new EmptyBorder(16, 12, 16, 12));

        // Brand / Title Section
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(10, 8, 20, 8));

        JLabel lblBrandTitle = new JLabel("STUDENT PORTAL");
        lblBrandTitle.setFont(UITheme.FONT_APP_TITLE);
        lblBrandTitle.setForeground(UITheme.COLOR_TEXT_WHITE);
        lblBrandTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBrandSubtitle = new JLabel("Management System");
        lblBrandSubtitle.setFont(UITheme.FONT_LABEL_REGULAR);
        lblBrandSubtitle.setForeground(new Color(148, 163, 184));
        lblBrandSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Total Students Active Badge
        lblSidebarTotalCount = new JLabel("● 0 Students Registered");
        lblSidebarTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSidebarTotalCount.setForeground(new Color(52, 211, 153)); // Light green dot
        lblSidebarTotalCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSidebarTotalCount.setBorder(new EmptyBorder(10, 0, 0, 0));

        brandPanel.add(lblBrandTitle);
        brandPanel.add(Box.createVerticalStrut(2));
        brandPanel.add(lblBrandSubtitle);
        brandPanel.add(Box.createVerticalStrut(4));
        brandPanel.add(lblSidebarTotalCount);

        // Navigation Buttons Container
        JPanel navButtonsPanel = new JPanel(new GridLayout(7, 1, 0, 8));
        navButtonsPanel.setOpaque(false);

        btnNavDashboard = createSidebarNavButton("Dashboard", "DASHBOARD");
        btnNavAdd = createSidebarNavButton("+ Add Student", "ADD");
        btnNavView = createSidebarNavButton("View Students", "VIEW");
        btnNavUpdate = createSidebarNavButton("Update Student", "UPDATE");
        btnNavDelete = createSidebarNavButton("Delete Student", "DELETE");
        btnNavSearch = createSidebarNavButton("Search Student", "SEARCH");
        btnNavAnalytics = createSidebarNavButton("Analytics & Stats", "ANALYTICS");

        navButtonsPanel.add(btnNavDashboard);
        navButtonsPanel.add(btnNavAdd);
        navButtonsPanel.add(btnNavView);
        navButtonsPanel.add(btnNavUpdate);
        navButtonsPanel.add(btnNavDelete);
        navButtonsPanel.add(btnNavSearch);
        navButtonsPanel.add(btnNavAnalytics);

        // Bottom Exit Button
        ModernButton btnExit = new ModernButton("Exit Application", ModernButton.ButtonType.DANGER);
        btnExit.setPreferredSize(new Dimension(200, 38));
        btnExit.addActionListener(e -> confirmAndExit());

        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(navButtonsPanel, BorderLayout.CENTER);
        sidebar.add(btnExit, BorderLayout.SOUTH);

        // Highlight dashboard initially
        highlightNavButton(btnNavDashboard);

        return sidebar;
    }

    private ModernButton createSidebarNavButton(String title, String cardKey) {
        ModernButton btn = new ModernButton(title, ModernButton.ButtonType.SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.addActionListener(e -> {
            cardLayout.show(contentArea, cardKey);
            highlightNavButton(btn);
            if ("VIEW".equals(cardKey)) {
                refreshViewTable();
            } else if ("DASHBOARD".equals(cardKey)) {
                refreshDashboardStats();
            } else if ("ANALYTICS".equals(cardKey)) {
                refreshAnalytics();
            }
        });
        return btn;
    }

    private void highlightNavButton(ModernButton selected) {
        ModernButton[] buttons = {btnNavDashboard, btnNavAdd, btnNavView, btnNavUpdate, btnNavDelete, btnNavSearch, btnNavAnalytics};
        for (ModernButton b : buttons) {
            if (b == selected) {
                b.setBackground(UITheme.COLOR_SIDEBAR_ACTIVE);
                b.setForeground(Color.WHITE);
            } else {
                b.setBackground(Color.BLACK); // Translucent
                b.setForeground(new Color(226, 232, 240));
            }
            b.repaint();
        }
    }

    // ==========================================
    // Center Content Container (CardLayout)
    // ==========================================

    private JPanel createCenterContainer() {
        contentArea.setBackground(UITheme.COLOR_BACKGROUND);

        contentArea.add(createDashboardPanel(), "DASHBOARD");
        contentArea.add(createAddStudentPanel(), "ADD");
        contentArea.add(createViewStudentsPanel(), "VIEW");
        contentArea.add(createUpdateStudentPanel(), "UPDATE");
        contentArea.add(createDeleteStudentPanel(), "DELETE");
        contentArea.add(createSearchStudentPanel(), "SEARCH");
        contentArea.add(createAnalyticsPanel(), "ANALYTICS");

        return contentArea;
    }

    // ==========================================
    // PANEL 1: DASHBOARD
    // ==========================================

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UITheme.COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Administrator Dashboard");
        lblTitle.setFont(UITheme.FONT_PAGE_HEADER);
        lblTitle.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Welcome back! Here is a summary of student enrollments and system status.");
        lblSub.setFont(UITheme.FONT_LABEL_REGULAR);
        lblSub.setForeground(UITheme.COLOR_TEXT_SECONDARY);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        // Cards Row
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setPreferredSize(new Dimension(cardsPanel.getWidth(), 105));

        lblStatTotal = new JLabel("0");
        lblStatDepts = new JLabel("0");
        lblStatMale = new JLabel("0");
        lblStatFemale = new JLabel("0");

        cardsPanel.add(createMetricCard("TOTAL STUDENTS", lblStatTotal, new Color(37, 99, 235), "Registered in System"));
        cardsPanel.add(createMetricCard("DEPARTMENTS", lblStatDepts, new Color(16, 185, 129), "Active Academic Depts"));
        cardsPanel.add(createMetricCard("MALE STUDENTS", lblStatMale, new Color(14, 165, 233), "Enrolled Males"));
        cardsPanel.add(createMetricCard("FEMALE STUDENTS", lblStatFemale, new Color(236, 72, 153), "Enrolled Females"));

        // Center Area: Quick Actions & Recent Students Table
        JPanel mainContent = new JPanel(new BorderLayout(16, 16));
        mainContent.setOpaque(false);

        // Quick Actions Bar
        JPanel quickActionsBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        quickActionsBar.setBackground(UITheme.COLOR_CARD_BG);
        quickActionsBar.setBorder(UITheme.createCardBorder());

        JLabel lblQuickActions = new JLabel("Quick Shortcuts: ");
        lblQuickActions.setFont(UITheme.FONT_LABEL_BOLD);
        lblQuickActions.setForeground(UITheme.COLOR_PRIMARY_DARK);

        ModernButton btnQuickAdd = new ModernButton("+ Add New Student", ModernButton.ButtonType.PRIMARY);
        btnQuickAdd.addActionListener(e -> {
            cardLayout.show(contentArea, "ADD");
            highlightNavButton(btnNavAdd);
        });

        ModernButton btnQuickView = new ModernButton("View Student Directory", ModernButton.ButtonType.OUTLINE);
        btnQuickView.addActionListener(e -> {
            cardLayout.show(contentArea, "VIEW");
            highlightNavButton(btnNavView);
            refreshViewTable();
        });

        ModernButton btnQuickSearch = new ModernButton("Search Student", ModernButton.ButtonType.SECONDARY);
        btnQuickSearch.addActionListener(e -> {
            cardLayout.show(contentArea, "SEARCH");
            highlightNavButton(btnNavSearch);
        });

        quickActionsBar.add(lblQuickActions);
        quickActionsBar.add(btnQuickAdd);
        quickActionsBar.add(btnQuickView);
        quickActionsBar.add(btnQuickSearch);

        // Recent Students Card
        JPanel recentCard = new JPanel(new BorderLayout(0, 10));
        recentCard.setBackground(UITheme.COLOR_CARD_BG);
        recentCard.setBorder(UITheme.createCardBorder());

        JLabel lblRecentTitle = new JLabel("Recent Student Registrations");
        lblRecentTitle.setFont(UITheme.FONT_CARD_TITLE);
        lblRecentTitle.setForeground(UITheme.COLOR_PRIMARY_DARK);

        String[] recentCols = {"Student ID", "Full Name", "Roll Number", "Department", "Year / Semester"};
        recentTableModel = new DefaultTableModel(recentCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ModernTable recentTable = new ModernTable(recentTableModel);
        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.COLOR_BORDER));

        recentCard.add(lblRecentTitle, BorderLayout.NORTH);
        recentCard.add(scrollPane, BorderLayout.CENTER);

        mainContent.add(quickActionsBar, BorderLayout.NORTH);
        mainContent.add(recentCard, BorderLayout.CENTER);

        // Assemble Dashboard Panel
        JPanel topContainer = new JPanel(new BorderLayout(0, 16));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(cardsPanel, BorderLayout.SOUTH);

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor, String footerText) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(UITheme.COLOR_CARD_BG);
        card.setBorder(UITheme.createCardBorder());

        JLabel lblCardTitle = new JLabel(title);
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblCardTitle.setForeground(UITheme.COLOR_TEXT_SECONDARY);

        valueLabel.setFont(UITheme.FONT_CARD_VALUE);
        valueLabel.setForeground(accentColor);

        JLabel lblFooter = new JLabel(footerText);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(UITheme.COLOR_TEXT_MUTED);

        card.add(lblCardTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(lblFooter, BorderLayout.SOUTH);

        return card;
    }

    // ==========================================
    // PANEL 2: ADD STUDENT
    // ==========================================

    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UITheme.COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Header
        JPanel header = createHeader("Add New Student",
                "Enter student personal and academic details. All fields are mandatory and validated.");

        // Form Card
        JPanel card = new JPanel(new BorderLayout(16, 16));
        card.setBackground(UITheme.COLOR_CARD_BG);
        card.setBorder(UITheme.createCardBorder());

        JPanel formGrid = new JPanel(new GridLayout(4, 2, 24, 16));
        formGrid.setOpaque(false);

        // Field 1: Student ID with Auto-Gen
        JPanel pnlId = new JPanel(new BorderLayout(6, 0));
        pnlId.setOpaque(false);
        txtAddId = createTextField("e.g., STU-1009");
        ModernButton btnAutoId = new ModernButton("Auto ID", ModernButton.ButtonType.SECONDARY);
        btnAutoId.setPreferredSize(new Dimension(85, 34));
        btnAutoId.addActionListener(e -> txtAddId.setText(generateNextStudentId()));
        pnlId.add(txtAddId, BorderLayout.CENTER);
        pnlId.add(btnAutoId, BorderLayout.EAST);
        formGrid.add(createFormField("Student ID *", pnlId));

        // Field 2: Full Name
        txtAddName = createTextField("e.g., Rahul Sharma");
        formGrid.add(createFormField("Full Name *", txtAddName));

        // Field 3: Roll Number
        txtAddRoll = createTextField("e.g., CS-2024-055");
        formGrid.add(createFormField("Roll Number *", txtAddRoll));

        // Field 4: Department ComboBox
        cmbAddDept = new JComboBox<>(DEPARTMENTS);
        styleComboBox(cmbAddDept);
        formGrid.add(createFormField("Department *", cmbAddDept));

        // Field 5: Year / Semester ComboBox
        cmbAddYear = new JComboBox<>(YEARS_SEMESTERS);
        styleComboBox(cmbAddYear);
        formGrid.add(createFormField("Year / Semester *", cmbAddYear));

        // Field 6: Email Address
        txtAddEmail = createTextField("e.g., rahul.sharma@college.edu");
        formGrid.add(createFormField("Email Address *", txtAddEmail));

        // Field 7: Mobile Number (10 digits)
        txtAddMobile = createTextField("10-digit mobile number, e.g., 9876543210");
        formGrid.add(createFormField("Mobile Number (10 digits) *", txtAddMobile));

        // Field 8: Gender ComboBox
        cmbAddGender = new JComboBox<>(GENDERS);
        styleComboBox(cmbAddGender);
        formGrid.add(createFormField("Gender *", cmbAddGender));

        // Buttons Bar
        JPanel buttonsBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonsBar.setOpaque(false);

        ModernButton btnClear = new ModernButton("Clear Form", ModernButton.ButtonType.SECONDARY);
        btnClear.setPreferredSize(new Dimension(130, 40));
        btnClear.addActionListener(e -> clearAddForm());

        ModernButton btnSubmit = new ModernButton("Add Student", ModernButton.ButtonType.PRIMARY);
        btnSubmit.setPreferredSize(new Dimension(150, 40));
        btnSubmit.addActionListener(e -> handleAddStudent());

        buttonsBar.add(btnClear);
        buttonsBar.add(btnSubmit);

        card.add(formGrid, BorderLayout.CENTER);
        card.add(buttonsBar, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private void handleAddStudent() {
        String id = txtAddId.getText().trim();
        String name = txtAddName.getText().trim();
        String roll = txtAddRoll.getText().trim();
        String dept = (String) cmbAddDept.getSelectedItem();
        String year = (String) cmbAddYear.getSelectedItem();
        String email = txtAddEmail.getText().trim();
        String mobile = txtAddMobile.getText().trim();
        String gender = (String) cmbAddGender.getSelectedItem();

        try {
            // Instantiate Student (Runs all encapsulated validations inside Person and Student)
            Student student = new Student(id, name, roll, dept, year, email, mobile, gender);

            // Add via StudentManager (Enforces unique ID and unique Roll Number)
            studentManager.add(student);

            // Show Success Notification
            JOptionPane.showMessageDialog(this,
                    "Student record added successfully!\n\n" +
                    "Student ID: " + student.getStudentId() + "\n" +
                    "Name: " + student.getFullName() + "\n" +
                    "Roll No: " + student.getRollNumber(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear inputs and refresh views
            clearAddForm();
            refreshAllDataViews();

            // Set status bar
            setStatusBar("Added student: " + student.getFullName() + " (" + student.getStudentId() + ")");

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAddForm() {
        txtAddId.setText("");
        txtAddName.setText("");
        txtAddRoll.setText("");
        cmbAddDept.setSelectedIndex(0);
        cmbAddYear.setSelectedIndex(0);
        txtAddEmail.setText("");
        txtAddMobile.setText("");
        cmbAddGender.setSelectedIndex(0);
    }

    private String generateNextStudentId() {
        int maxNum = 1000;
        for (Student s : studentManager.getAll()) {
            String id = s.getStudentId();
            if (id.startsWith("STU-")) {
                try {
                    int num = Integer.parseInt(id.substring(4));
                    if (num > maxNum) maxNum = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return "STU-" + (maxNum + 1);
    }

    // ==========================================
    // PANEL 3: VIEW ALL STUDENTS
    // ==========================================

    private JPanel createViewStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UITheme.COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Header
        JPanel header = createHeader("Student Directory",
                "View, filter, and inspect all student records in real time.");

        // Main Table Card
        JPanel tableCard = new JPanel(new BorderLayout(12, 12));
        tableCard.setBackground(UITheme.COLOR_CARD_BG);
        tableCard.setBorder(UITheme.createCardBorder());

        // Filter / Search Toolbar
        JPanel filterToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterToolbar.setOpaque(false);

        JLabel lblSearch = new JLabel("Filter:");
        lblSearch.setFont(UITheme.FONT_LABEL_BOLD);

        txtViewSearch = createTextField("Search name, roll, email...");
        txtViewSearch.setPreferredSize(new Dimension(200, 34));
        txtViewSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyViewFilters();
            }
        });

        // Filter by Department
        String[] deptOptions = new String[DEPARTMENTS.length + 1];
        deptOptions[0] = "All Departments";
        System.arraycopy(DEPARTMENTS, 0, deptOptions, 1, DEPARTMENTS.length);
        cmbViewFilterDept = new JComboBox<>(deptOptions);
        styleComboBox(cmbViewFilterDept);
        cmbViewFilterDept.addActionListener(e -> applyViewFilters());

        // Filter by Year
        String[] yearOptions = new String[YEARS_SEMESTERS.length + 1];
        yearOptions[0] = "All Semesters";
        System.arraycopy(YEARS_SEMESTERS, 0, yearOptions, 1, YEARS_SEMESTERS.length);
        cmbViewFilterYear = new JComboBox<>(yearOptions);
        styleComboBox(cmbViewFilterYear);
        cmbViewFilterYear.addActionListener(e -> applyViewFilters());

        ModernButton btnResetFilter = new ModernButton("Reset", ModernButton.ButtonType.SECONDARY);
        btnResetFilter.setPreferredSize(new Dimension(80, 34));
        btnResetFilter.addActionListener(e -> {
            txtViewSearch.setText("");
            cmbViewFilterDept.setSelectedIndex(0);
            cmbViewFilterYear.setSelectedIndex(0);
            applyViewFilters();
        });

        filterToolbar.add(lblSearch);
        filterToolbar.add(txtViewSearch);
        filterToolbar.add(new JLabel("Dept:"));
        filterToolbar.add(cmbViewFilterDept);
        filterToolbar.add(new JLabel("Semester:"));
        filterToolbar.add(cmbViewFilterYear);
        filterToolbar.add(btnResetFilter);

        // Table Setup
        viewTableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        viewTable = new ModernTable(viewTableModel);
        JScrollPane scrollPane = new JScrollPane(viewTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.COLOR_BORDER));

        // Bottom Action Toolbar
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);

        lblViewRecordCount = new JLabel("Showing 0 students");
        lblViewRecordCount.setFont(UITheme.FONT_LABEL_REGULAR);
        lblViewRecordCount.setForeground(UITheme.COLOR_TEXT_SECONDARY);

        JPanel btnActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnActions.setOpaque(false);

        ModernButton btnRefresh = new ModernButton("Refresh Table", ModernButton.ButtonType.SECONDARY);
        btnRefresh.addActionListener(e -> refreshViewTable());

        ModernButton btnEditSelected = new ModernButton("Edit Selected", ModernButton.ButtonType.PRIMARY);
        btnEditSelected.addActionListener(e -> handleEditSelectedFromTable());

        ModernButton btnDeleteSelected = new ModernButton("Delete Selected", ModernButton.ButtonType.DANGER);
        btnDeleteSelected.addActionListener(e -> handleDeleteSelectedFromTable());

        btnActions.add(btnRefresh);
        btnActions.add(btnEditSelected);
        btnActions.add(btnDeleteSelected);

        bottomBar.add(lblViewRecordCount, BorderLayout.WEST);
        bottomBar.add(btnActions, BorderLayout.EAST);

        tableCard.add(filterToolbar, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        tableCard.add(bottomBar, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);

        return panel;
    }

    private void applyViewFilters() {
        String keyword = txtViewSearch.getText().trim();
        String selDept = (String) cmbViewFilterDept.getSelectedItem();
        String selYear = (String) cmbViewFilterYear.getSelectedItem();

        String deptFilter = "All Departments".equals(selDept) ? null : selDept;
        String yearFilter = "All Semesters".equals(selYear) ? null : selYear;

        List<Student> results;
        if (keyword.isEmpty()) {
            results = studentManager.filter(deptFilter, yearFilter);
        } else {
            // Apply keyword search then filter
            results = studentManager.search(keyword);
            if (deptFilter != null || yearFilter != null) {
                results.removeIf(s -> (deptFilter != null && !s.getDepartment().equalsIgnoreCase(deptFilter)) ||
                                      (yearFilter != null && !s.getYearSemester().equalsIgnoreCase(yearFilter)));
            }
        }

        populateTableModel(viewTableModel, results);
        lblViewRecordCount.setText("Showing " + results.size() + " of " + studentManager.count() + " students");
    }

    private void handleEditSelectedFromTable() {
        int selectedRow = viewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student from the table to edit.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) viewTableModel.getValueAt(selectedRow, 0);
        cardLayout.show(contentArea, "UPDATE");
        highlightNavButton(btnNavUpdate);
        txtUpdateSearchId.setText(studentId);
        loadStudentForUpdate(studentId);
    }

    private void handleDeleteSelectedFromTable() {
        int selectedRow = viewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student from the table to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) viewTableModel.getValueAt(selectedRow, 0);
        String studentName = (String) viewTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student record?\n\n" +
                "ID: " + studentId + "\n" +
                "Name: " + studentName + "\n\n" +
                "This action cannot be undone.",
                "Confirm Student Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = studentManager.delete(studentId);
                if (deleted) {
                    JOptionPane.showMessageDialog(this,
                            "Student record deleted successfully.",
                            "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    refreshAllDataViews();
                    setStatusBar("Deleted student: " + studentName + " (" + studentId + ")");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Student could not be found for deletion.",
                            "Not Found", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==========================================
    // PANEL 4: UPDATE STUDENT
    // ==========================================

    private JPanel createUpdateStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UITheme.COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = createHeader("Update Student Record",
                "Search student by ID, review their existing information, and save modified details.");

        // Main Card
        JPanel card = new JPanel(new BorderLayout(16, 16));
        card.setBackground(UITheme.COLOR_CARD_BG);
        card.setBorder(UITheme.createCardBorder());

        // Top Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        searchBar.setOpaque(false);

        JLabel lblSearch = new JLabel("Enter Student ID to Edit: ");
        lblSearch.setFont(UITheme.FONT_LABEL_BOLD);

        txtUpdateSearchId = createTextField("e.g., STU-1001");
        txtUpdateSearchId.setPreferredSize(new Dimension(200, 36));

        ModernButton btnSearch = new ModernButton("Load Student", ModernButton.ButtonType.PRIMARY);
        btnSearch.setPreferredSize(new Dimension(130, 36));
        btnSearch.addActionListener(e -> loadStudentForUpdate(txtUpdateSearchId.getText().trim()));

        searchBar.add(lblSearch);
        searchBar.add(txtUpdateSearchId);
        searchBar.add(btnSearch);

        // Editable Form
        JPanel formGrid = new JPanel(new GridLayout(4, 2, 24, 16));
        formGrid.setOpaque(false);

        txtUpdateId = createTextField("");
        txtUpdateId.setEditable(false);
        txtUpdateId.setBackground(new Color(241, 245, 249)); // Read-only styling
        formGrid.add(createFormField("Student ID (Read-Only)", txtUpdateId));

        txtUpdateName = createTextField("");
        formGrid.add(createFormField("Full Name *", txtUpdateName));

        txtUpdateRoll = createTextField("");
        formGrid.add(createFormField("Roll Number *", txtUpdateRoll));

        cmbUpdateDept = new JComboBox<>(DEPARTMENTS);
        styleComboBox(cmbUpdateDept);
        formGrid.add(createFormField("Department *", cmbUpdateDept));

        cmbUpdateYear = new JComboBox<>(YEARS_SEMESTERS);
        styleComboBox(cmbUpdateYear);
        formGrid.add(createFormField("Year / Semester *", cmbUpdateYear));

        txtUpdateEmail = createTextField("");
        formGrid.add(createFormField("Email Address *", txtUpdateEmail));

        txtUpdateMobile = createTextField("");
        formGrid.add(createFormField("Mobile Number *", txtUpdateMobile));

        cmbUpdateGender = new JComboBox<>(GENDERS);
        styleComboBox(cmbUpdateGender);
        formGrid.add(createFormField("Gender *", cmbUpdateGender));

        // Action Buttons
        JPanel buttonsBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonsBar.setOpaque(false);

        ModernButton btnCancel = new ModernButton("Reset / Clear", ModernButton.ButtonType.SECONDARY);
        btnCancel.setPreferredSize(new Dimension(130, 40));
        btnCancel.addActionListener(e -> clearUpdateForm());

        btnUpdateSave = new ModernButton("Save Updates", ModernButton.ButtonType.PRIMARY);
        btnUpdateSave.setPreferredSize(new Dimension(150, 40));
        btnUpdateSave.setEnabled(false);
        btnUpdateSave.addActionListener(e -> handleSaveUpdate());

        buttonsBar.add(btnCancel);
        buttonsBar.add(btnUpdateSave);

        card.add(searchBar, BorderLayout.NORTH);
        card.add(formGrid, BorderLayout.CENTER);
        card.add(buttonsBar, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private void loadStudentForUpdate(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Student ID to search.",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = studentManager.findById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(this,
                    "No student found with ID: " + studentId,
                    "Student Not Found", JOptionPane.ERROR_MESSAGE);
            clearUpdateForm();
            return;
        }

        // Populate fields
        txtUpdateId.setText(student.getStudentId());
        txtUpdateName.setText(student.getFullName());
        txtUpdateRoll.setText(student.getRollNumber());
        cmbUpdateDept.setSelectedItem(student.getDepartment());
        cmbUpdateYear.setSelectedItem(student.getYearSemester());
        txtUpdateEmail.setText(student.getEmail());
        txtUpdateMobile.setText(student.getMobileNumber());
        cmbUpdateGender.setSelectedItem(student.getGender());

        btnUpdateSave.setEnabled(true);
        setStatusBar("Loaded student for update: " + student.getFullName());
    }

    private void handleSaveUpdate() {
        String id = txtUpdateId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load a student before saving updates.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = txtUpdateName.getText().trim();
        String roll = txtUpdateRoll.getText().trim();
        String dept = (String) cmbUpdateDept.getSelectedItem();
        String year = (String) cmbUpdateYear.getSelectedItem();
        String email = txtUpdateEmail.getText().trim();
        String mobile = txtUpdateMobile.getText().trim();
        String gender = (String) cmbUpdateGender.getSelectedItem();

        try {
            Student updated = new Student(id, name, roll, dept, year, email, mobile, gender);
            studentManager.update(updated);

            JOptionPane.showMessageDialog(this,
                    "Student details updated successfully!\n\n" +
                    "ID: " + updated.getStudentId() + "\n" +
                    "Name: " + updated.getFullName(),
                    "Update Successful", JOptionPane.INFORMATION_MESSAGE);

            refreshAllDataViews();
            setStatusBar("Updated student details for: " + updated.getFullName());

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to update student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearUpdateForm() {
        txtUpdateSearchId.setText("");
        txtUpdateId.setText("");
        txtUpdateName.setText("");
        txtUpdateRoll.setText("");
        cmbUpdateDept.setSelectedIndex(0);
        cmbUpdateYear.setSelectedIndex(0);
        txtUpdateEmail.setText("");
        txtUpdateMobile.setText("");
        cmbUpdateGender.setSelectedIndex(0);
        btnUpdateSave.setEnabled(false);
    }

    // ==========================================
    // PANEL 5: DELETE STUDENT
    // ==========================================

    private JPanel createDeleteStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UITheme.COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = createHeader("Delete Student Record",
                "Permanently delete a student record from the system with verification safeguards.");

        JPanel card = new JPanel(new BorderLayout(16, 16));
        card.setBackground(UITheme.COLOR_CARD_BG);
        card.setBorder(UITheme.createCardBorder());

        // Top Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        searchBar.setOpaque(false);

        JLabel lblSearch = new JLabel("Enter Student ID to Delete: ");
        lblSearch.setFont(UITheme.FONT_LABEL_BOLD);

        txtDeleteSearchId = createTextField("e.g., STU-1001");
        txtDeleteSearchId.setPreferredSize(new Dimension(200, 36));

        ModernButton btnSearch = new ModernButton("Search Student", ModernButton.ButtonType.PRIMARY);
        btnSearch.setPreferredSize(new Dimension(140, 36));
        btnSearch.addActionListener(e -> loadStudentForDelete(txtDeleteSearchId.getText().trim()));

        searchBar.add(lblSearch);
        searchBar.add(txtDeleteSearchId);
        searchBar.add(btnSearch);

        // Center Details Preview Card
        pnlDeleteStudentCard = new JPanel(new GridLayout(6, 2, 16, 12));
        pnlDeleteStudentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.COLOR_BORDER, 1),
                new EmptyBorder(20, 24, 20, 24)
        ));
        pnlDeleteStudentCard.setBackground(new Color(248, 250, 252));

        lblDeleteInfoId = new JLabel("-");
        lblDeleteInfoName = new JLabel("-");
        lblDeleteInfoRoll = new JLabel("-");
        lblDeleteInfoDept = new JLabel("-");
        lblDeleteInfoEmail = new JLabel("-");
        lblDeleteInfoMobile = new JLabel("-");

        addInfoRow(pnlDeleteStudentCard, "Student ID:", lblDeleteInfoId);
        addInfoRow(pnlDeleteStudentCard, "Full Name:", lblDeleteInfoName);
        addInfoRow(pnlDeleteStudentCard, "Roll Number:", lblDeleteInfoRoll);
        addInfoRow(pnlDeleteStudentCard, "Department:", lblDeleteInfoDept);
        addInfoRow(pnlDeleteStudentCard, "Email Address:", lblDeleteInfoEmail);
        addInfoRow(pnlDeleteStudentCard, "Mobile Number:", lblDeleteInfoMobile);

        // Action Buttons Bar
        JPanel buttonsBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonsBar.setOpaque(false);

        btnDeleteConfirm = new ModernButton("Delete Record Permanently", ModernButton.ButtonType.DANGER);
        btnDeleteConfirm.setPreferredSize(new Dimension(220, 42));
        btnDeleteConfirm.setEnabled(false);
        btnDeleteConfirm.addActionListener(e -> executeDeleteStudent());

        buttonsBar.add(btnDeleteConfirm);

        card.add(searchBar, BorderLayout.NORTH);
        card.add(pnlDeleteStudentCard, BorderLayout.CENTER);
        card.add(buttonsBar, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private void addInfoRow(JPanel container, String labelText, JLabel valueLabel) {
        JLabel lblTitle = new JLabel(labelText);
        lblTitle.setFont(UITheme.FONT_LABEL_BOLD);
        lblTitle.setForeground(UITheme.COLOR_PRIMARY_DARK);

        valueLabel.setFont(UITheme.FONT_LABEL_REGULAR);
        valueLabel.setForeground(UITheme.COLOR_TEXT_PRIMARY);

        container.add(lblTitle);
        container.add(valueLabel);
    }

    private void loadStudentForDelete(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = studentManager.findById(studentId);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID: " + studentId, "Not Found", JOptionPane.ERROR_MESSAGE);
            clearDeleteView();
            return;
        }

        currentStudentIdToDelete = s.getStudentId();
        lblDeleteInfoId.setText(s.getStudentId());
        lblDeleteInfoName.setText(s.getFullName());
        lblDeleteInfoRoll.setText(s.getRollNumber());
        lblDeleteInfoDept.setText(s.getDepartment() + " (" + s.getYearSemester() + ")");
        lblDeleteInfoEmail.setText(s.getEmail());
        lblDeleteInfoMobile.setText(s.getMobileNumber() + " (" + s.getGender() + ")");

        btnDeleteConfirm.setEnabled(true);
        setStatusBar("Found student for deletion: " + s.getFullName());
    }

    private void executeDeleteStudent() {
        if (currentStudentIdToDelete == null) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "CONFIRM DELETION:\n\n" +
                "Are you sure you want to delete student ID: " + currentStudentIdToDelete + "?\n" +
                "Name: " + lblDeleteInfoName.getText() + "\n\n" +
                "This action cannot be reverted.",
                "Confirm Student Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean removed = studentManager.delete(currentStudentIdToDelete);
                if (removed) {
                    JOptionPane.showMessageDialog(this,
                            "Student record deleted successfully.",
                            "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearDeleteView();
                    refreshAllDataViews();
                    setStatusBar("Deleted student: " + currentStudentIdToDelete);
                } else {
                    JOptionPane.showMessageDialog(this, "Student could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearDeleteView() {
        txtDeleteSearchId.setText("");
        currentStudentIdToDelete = null;
        lblDeleteInfoId.setText("-");
        lblDeleteInfoName.setText("-");
        lblDeleteInfoRoll.setText("-");
        lblDeleteInfoDept.setText("-");
        lblDeleteInfoEmail.setText("-");
        lblDeleteInfoMobile.setText("-");
        btnDeleteConfirm.setEnabled(false);
    }

    // ==========================================
    // PANEL 6: SEARCH STUDENT (OOP: Method Overloading)
    // ==========================================

    private JPanel createSearchStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UITheme.COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = createHeader("Search Students",
                "Perform flexible multi-criteria searches by Student ID, Full Name, Roll Number, or Department.");

        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(UITheme.COLOR_CARD_BG);
        card.setBorder(UITheme.createCardBorder());

        // Search Controls
        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        searchControls.setOpaque(false);

        JLabel lblCriteria = new JLabel("Search By:");
        lblCriteria.setFont(UITheme.FONT_LABEL_BOLD);

        String[] criteriaOptions = {"All Fields", "Student ID", "Full Name", "Roll Number", "Department"};
        cmbSearchCriteria = new JComboBox<>(criteriaOptions);
        styleComboBox(cmbSearchCriteria);

        txtSearchQuery = createTextField("Type search keyword here...");
        txtSearchQuery.setPreferredSize(new Dimension(240, 36));
        txtSearchQuery.addActionListener(e -> executeSearch());

        ModernButton btnSearch = new ModernButton("Search", ModernButton.ButtonType.PRIMARY);
        btnSearch.setPreferredSize(new Dimension(110, 36));
        btnSearch.addActionListener(e -> executeSearch());

        ModernButton btnClear = new ModernButton("Reset", ModernButton.ButtonType.SECONDARY);
        btnClear.setPreferredSize(new Dimension(90, 36));
        btnClear.addActionListener(e -> {
            txtSearchQuery.setText("");
            cmbSearchCriteria.setSelectedIndex(0);
            executeSearch();
        });

        searchControls.add(lblCriteria);
        searchControls.add(cmbSearchCriteria);
        searchControls.add(txtSearchQuery);
        searchControls.add(btnSearch);
        searchControls.add(btnClear);

        // Results Table
        searchTableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        searchTable = new ModernTable(searchTableModel);
        JScrollPane scrollPane = new JScrollPane(searchTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.COLOR_BORDER));

        // Bottom Status
        lblSearchStatus = new JLabel("Enter search query above and click Search.");
        lblSearchStatus.setFont(UITheme.FONT_LABEL_REGULAR);
        lblSearchStatus.setForeground(UITheme.COLOR_TEXT_SECONDARY);

        card.add(searchControls, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(lblSearchStatus, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private void executeSearch() {
        String query = txtSearchQuery.getText().trim();
        String criteria = (String) cmbSearchCriteria.getSelectedItem();

        List<Student> results;
        if (query.isEmpty()) {
            results = studentManager.getAll();
            lblSearchStatus.setText("Displaying all " + results.size() + " student records.");
        } else {
            // Demonstrates OOP Method Overloading in StudentManager
            if ("All Fields".equalsIgnoreCase(criteria)) {
                results = studentManager.search(query);
            } else {
                results = studentManager.search(query, criteria);
            }

            if (results.isEmpty()) {
                lblSearchStatus.setText("No students found matching '" + query + "' under criteria '" + criteria + "'.");
                JOptionPane.showMessageDialog(this,
                        "No student records matched your search query:\n\"" + query + "\" (" + criteria + ")",
                        "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                lblSearchStatus.setText("Found " + results.size() + " matching student(s).");
            }
        }

        populateTableModel(searchTableModel, results);
    }

    // ==========================================
    // PANEL 7: ANALYTICS & REPORTS
    // ==========================================

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UITheme.COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = createHeader("Analytics & Department Overview",
                "Aggregated statistics, department breakdown, and student demographics.");

        JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
        content.setOpaque(false);

        // Left Card: Department Breakdown Table
        JPanel deptCard = new JPanel(new BorderLayout(12, 12));
        deptCard.setBackground(UITheme.COLOR_CARD_BG);
        deptCard.setBorder(UITheme.createCardBorder());

        JLabel lblDeptTitle = new JLabel("Enrollment by Department");
        lblDeptTitle.setFont(UITheme.FONT_CARD_TITLE);
        lblDeptTitle.setForeground(UITheme.COLOR_PRIMARY_DARK);

        String[] deptCols = {"Department Name", "Student Count", "Percentage"};
        analyticsTableModel = new DefaultTableModel(deptCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ModernTable deptTable = new ModernTable(analyticsTableModel);
        JScrollPane scrollDept = new JScrollPane(deptTable);
        scrollDept.setBorder(BorderFactory.createLineBorder(UITheme.COLOR_BORDER));

        deptCard.add(lblDeptTitle, BorderLayout.NORTH);
        deptCard.add(scrollDept, BorderLayout.CENTER);

        // Right Card: Demographics & Storage Info
        JPanel demoCard = new JPanel();
        demoCard.setLayout(new BoxLayout(demoCard, BoxLayout.Y_AXIS));
        demoCard.setBackground(UITheme.COLOR_CARD_BG);
        demoCard.setBorder(UITheme.createCardBorder());

        JLabel lblDemoTitle = new JLabel("Demographics & Storage");
        lblDemoTitle.setFont(UITheme.FONT_CARD_TITLE);
        lblDemoTitle.setForeground(UITheme.COLOR_PRIMARY_DARK);
        lblDemoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblAnalyticsMale = new JLabel("Male Students: 0");
        lblAnalyticsMale.setFont(UITheme.FONT_LABEL_BOLD);
        lblAnalyticsMale.setForeground(new Color(14, 165, 233));
        lblAnalyticsMale.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblAnalyticsFemale = new JLabel("Female Students: 0");
        lblAnalyticsFemale.setFont(UITheme.FONT_LABEL_BOLD);
        lblAnalyticsFemale.setForeground(new Color(236, 72, 153));
        lblAnalyticsFemale.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblAnalyticsOther = new JLabel("Other / Unspecified: 0");
        lblAnalyticsOther.setFont(UITheme.FONT_LABEL_BOLD);
        lblAnalyticsOther.setForeground(new Color(100, 116, 139));
        lblAnalyticsOther.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblStorageInfo = new JLabel("<html><b>Data Storage Engine:</b><br/>" +
                "Format: CSV (Comma-Separated Values)<br/>" +
                "File Path: " + studentManager.getStoragePath() + "<br/>" +
                "Persistence: Real-time atomic read/write on every CRUD operation.<br/>" +
                "Encapsulation: Object-oriented model with defensive copies.<br/>" +
                "Thread Safety: Synchronized data manager.</html>");
        lblStorageInfo.setFont(UITheme.FONT_LABEL_REGULAR);
        lblStorageInfo.setForeground(UITheme.COLOR_TEXT_SECONDARY);
        lblStorageInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStorageInfo.setBorder(new EmptyBorder(16, 0, 0, 0));

        demoCard.add(lblDemoTitle);
        demoCard.add(Box.createVerticalStrut(14));
        demoCard.add(lblAnalyticsMale);
        demoCard.add(Box.createVerticalStrut(8));
        demoCard.add(lblAnalyticsFemale);
        demoCard.add(Box.createVerticalStrut(8));
        demoCard.add(lblAnalyticsOther);
        demoCard.add(Box.createVerticalStrut(20));
        demoCard.add(new JSeparator(SwingConstants.HORIZONTAL));
        demoCard.add(lblStorageInfo);

        content.add(deptCard);
        content.add(demoCard);

        panel.add(header, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private void refreshAnalytics() {
        int total = studentManager.count();
        Map<String, Integer> deptMap = studentManager.getDepartmentDistribution();

        analyticsTableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : deptMap.entrySet()) {
            double percent = (total > 0) ? ((double) entry.getValue() / total) * 100.0 : 0.0;
            analyticsTableModel.addRow(new Object[]{
                    entry.getKey(),
                    entry.getValue(),
                    String.format("%.1f%%", percent)
            });
        }

        Map<String, Integer> genderMap = studentManager.getGenderDistribution();
        int male = genderMap.getOrDefault("Male", 0);
        int female = genderMap.getOrDefault("Female", 0);
        int other = genderMap.getOrDefault("Other", 0);

        lblAnalyticsMale.setText("Male Students: " + male + ((total > 0) ? String.format(" (%.1f%%)", (male * 100.0 / total)) : ""));
        lblAnalyticsFemale.setText("Female Students: " + female + ((total > 0) ? String.format(" (%.1f%%)", (female * 100.0 / total)) : ""));
        lblAnalyticsOther.setText("Other / Unspecified: " + other);
    }

    // ==========================================
    // Status Bar & Helpers
    // ==========================================

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setPreferredSize(new Dimension(getWidth(), 26));
        bar.setBackground(new Color(241, 245, 249));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.COLOR_BORDER),
                new EmptyBorder(2, 16, 2, 16)
        ));

        lblStatusBarMessage = new JLabel("System Ready.");
        lblStatusBarMessage.setFont(UITheme.FONT_STATUS_BAR);
        lblStatusBarMessage.setForeground(UITheme.COLOR_TEXT_SECONDARY);

        lblStatusBarPath = new JLabel("Storage: " + studentManager.getStoragePath());
        lblStatusBarPath.setFont(UITheme.FONT_STATUS_BAR);
        lblStatusBarPath.setForeground(UITheme.COLOR_TEXT_MUTED);

        bar.add(lblStatusBarMessage, BorderLayout.WEST);
        bar.add(lblStatusBarPath, BorderLayout.EAST);

        return bar;
    }

    private void setStatusBar(String message) {
        lblStatusBarMessage.setText(message);
    }

    private JPanel createHeader(String titleText, String subtitleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel(titleText);
        title.setFont(UITheme.FONT_PAGE_HEADER);
        title.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setFont(UITheme.FONT_LABEL_REGULAR);
        subtitle.setForeground(UITheme.COLOR_TEXT_SECONDARY);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        return header;
    }

    private JPanel createFormField(String labelText, JComponent inputComponent) {
        JPanel fieldPanel = new JPanel(new BorderLayout(0, 5));
        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.FONT_LABEL_BOLD);
        label.setForeground(UITheme.COLOR_TEXT_PRIMARY);

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(inputComponent, BorderLayout.CENTER);
        return fieldPanel;
    }

    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(UITheme.FONT_INPUT);
        tf.setBorder(UITheme.createFieldBorder());
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 36));
        return tf;
    }

    private void styleComboBox(JComboBox<?> cb) {
        cb.setFont(UITheme.FONT_INPUT);
        cb.setBackground(Color.WHITE);
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 36));
    }

    private void populateTableModel(DefaultTableModel model, List<Student> students) {
        model.setRowCount(0);
        if (students != null) {
            for (Student s : students) {
                model.addRow(new Object[]{
                        s.getStudentId(),
                        s.getFullName(),
                        s.getRollNumber(),
                        s.getDepartment(),
                        s.getYearSemester(),
                        s.getEmail(),
                        s.getMobileNumber(),
                        s.getGender()
                });
            }
        }
    }

    private void refreshAllDataViews() {
        refreshDashboardStats();
        refreshViewTable();
        refreshAnalytics();
        lblSidebarTotalCount.setText("● " + studentManager.count() + " Students Registered");
    }

    private void refreshDashboardStats() {
        int total = studentManager.count();
        lblStatTotal.setText(String.valueOf(total));
        lblStatDepts.setText(String.valueOf(studentManager.getDepartmentDistribution().size()));

        Map<String, Integer> genderMap = studentManager.getGenderDistribution();
        lblStatMale.setText(String.valueOf(genderMap.getOrDefault("Male", 0)));
        lblStatFemale.setText(String.valueOf(genderMap.getOrDefault("Female", 0)));

        // Recent students (last 5)
        List<Student> recent = studentManager.getRecentStudents(5);
        recentTableModel.setRowCount(0);
        for (Student s : recent) {
            recentTableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getFullName(),
                    s.getRollNumber(),
                    s.getDepartment(),
                    s.getYearSemester()
            });
        }
    }

    private void refreshViewTable() {
        applyViewFilters();
    }

    private void confirmAndExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the Student Management System?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}

