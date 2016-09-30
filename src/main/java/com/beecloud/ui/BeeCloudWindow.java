/**
 * 
 */
package com.beecloud.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.beecloud.api.JiraRestClientFactory;
import com.beecloud.api.JiraUtil;
import com.beecloud.excel.ExcelParser;
import com.beecloud.log.LogOutStream;
import com.beecloud.model.TestCase;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年9月9日 上午9:43:57
 * @version v1.0
 */
public class BeeCloudWindow extends JFrame implements ActionListener,ItemListener,KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 319989898114382566L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private JButton Button_SelectFile_1;
	private JTextArea JTextArea_filePath_1;
	public static JTextArea JTextArea_logtout_1;
	private int width = 0;
	private int height = 0;
	private JiraRestClient restClient = null;
	private JComboBox comboBox;//项目下拉选项框
	private String projectName;
	public BeeCloudWindow() {
		initWindow();
		initPorjectComboBox();
		JScrollPane js = new JScrollPane(JTextArea_logtout_1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(js, GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(Button_SelectFile_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(JTextArea_filePath_1, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
						.addComponent(comboBox, 0, 429, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(JTextArea_filePath_1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(Button_SelectFile_1))
					.addGap(9)
					.addComponent(js, GroupLayout.PREFERRED_SIZE, 577, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
	
	/**
	 * 初始化控件
	 */
	public void initWindow() {
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		int pc_bottom_height = screenInsets.bottom;
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		width = scrSize.width/3;
		height = scrSize.height-pc_bottom_height;
		Button_SelectFile_1 = new JButton("选择文件：");
		Button_SelectFile_1.addActionListener(this);
		comboBox = new JComboBox();
		comboBox.setBorder(BorderFactory.createTitledBorder("项目选择"));
		comboBox.addItemListener(this);
		JTextArea_filePath_1 = new JTextArea();
		JTextArea_filePath_1.setEditable(false);//文件选择框设置为不可编辑
		JTextArea_logtout_1 = new JTextArea();
		JTextArea_logtout_1.addKeyListener(this);
		JTextArea_logtout_1.setFont(new Font("楷体", Font.PLAIN, 13));
		JTextArea_logtout_1.setLineWrap(true);
		JTextArea_logtout_1.setBorder(BorderFactory.createTitledBorder("日志输出"));
		this.setTitle("云蜂用例导入工具");
		this.setSize(width, height);
		this.setResizable(false);//设置窗体不能变化大小
		this.setLocationRelativeTo(null);//设置窗体居中显示
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口右上角的关闭按钮关闭窗口,退出程序
		this.startLogforward();
		try {
			restClient = JiraRestClientFactory.CreateJiraRestClient();
			logger.info("Initialization Complete");
		} catch (Exception e) {
			logger.info("Initialization Failed");
		}
	}
	
	
	/**
	 * 初始化项目列表
	 * @return
	 */
	public void initPorjectComboBox(){
		List<String> list = null;
		JiraUtil jiraUtil = new JiraUtil(restClient);
		try {
			list = jiraUtil.getAllProject();
		} catch (Exception e) {
			logger.error("Failed to get the list of projects");
		}
		if(list.size()>0){
			for(String item:list) {
				comboBox.addItem(item.toString());
			}
		}
	}
	
	public static void main(String[] args) {
		new BeeCloudWindow().show();
	}
	
	
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Button_SelectFile_1) {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(null);
			if(result==JFileChooser.APPROVE_OPTION ) { //点击确认按钮
				final String path = fileChooser.getSelectedFile().getAbsolutePath(); 
				JTextArea_filePath_1.setText(path);
				logger.info("selected file:"+path);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							ExcelParser ep = new ExcelParser(path);
							List<TestCase> list = ep.transRowsToCase(projectName);
							JiraUtil jiraUtil = new JiraUtil(restClient);
							if(list.size()>0){
								logger.info("TestCase importer is starting...");
								logger.info("addIssue for project:"+projectName);
								for(TestCase testCase:list){
									logger.info(testCase.toString());
									jiraUtil.AddIssue(projectName, testCase);
								}
								logger.info("Import Completed");
							}
						} catch (Exception e1) {
							logger.error(e1.toString());
						}
					}}).start();
			}
		}
	}
	
	/**
	 * System输出重定向
	 */
	public void startLogforward(){
		LogOutStream out = new LogOutStream(System.out);
		System.setOut(out);
	}

	/* （非 Javadoc）
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==comboBox){
			 if(e.getStateChange() == ItemEvent.SELECTED){
				 projectName = comboBox.getSelectedItem().toString();
				 logger.info("The Project you Selected is:"+comboBox.getSelectedItem());
			 }			
		}
	}

	/* （非 Javadoc）
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && e.VK_D == e.getKeyCode()){				//CTRL+D  清空
			JTextArea_logtout_1.setText("");
	        return;
		}
	}

	/* （非 Javadoc）
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
	}

	/* （非 Javadoc）
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
