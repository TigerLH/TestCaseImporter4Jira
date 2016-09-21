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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.beecloud.api.JiraUtil;
import com.beecloud.auth.JiraRestClientFactory;
import com.beecloud.data.ExcelParser;
import com.beecloud.log.LogOutStream;
import com.beecloud.model.TestCase;

import javax.swing.JLabel;
import javax.swing.JComboBox;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年9月9日 上午9:43:57
 * @version v1.0
 */
public class BeeCloudWindow extends JFrame implements ActionListener,ItemListener{
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
		JLabel label = new JLabel("选择项目：");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(js)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(Button_SelectFile_1)
								.addComponent(label))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox, 0, 326, Short.MAX_VALUE)
								.addComponent(JTextArea_filePath_1, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(23)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(Button_SelectFile_1)
						.addComponent(JTextArea_filePath_1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
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
		comboBox.addItemListener(this);
		JTextArea_filePath_1 = new JTextArea();
		JTextArea_filePath_1.setEditable(false);//文件选择框设置为不可编辑
		JTextArea_logtout_1 = new JTextArea();
		JTextArea_logtout_1.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		JTextArea_logtout_1.setLineWrap(true);
		JTextArea_logtout_1.setBorder(BorderFactory.createTitledBorder("日志输出"));
		this.setTitle("云蜂用例导入工具");
		this.setSize(width, height);
		this.setResizable(false);//设置窗体不能变化大小
		this.setLocationRelativeTo(null);//设置窗体居中显示
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口右上角的关闭按钮关闭窗口,退出程序
		//this.startLogforward();
		try {
			restClient = JiraRestClientFactory.CreateJiraRestClient();
			logger.info("初始化完成");
		} catch (Exception e) {
			logger.info("捕获异常");
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
			logger.error("项目列表获取失败");
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
				try {
					ExcelParser ep = new ExcelParser(path);
					List<TestCase> list = ep.transRowsToCase(projectName);
					JiraUtil jiraUtil = new JiraUtil(restClient);
					for(TestCase testCase:list){
						logger.info("addIssue for priject:"+projectName);
						logger.info(testCase.toString());
						jiraUtil.AddIssue(projectName, testCase);
					}
				} catch (Exception e1) {
					logger.error(e1.getMessage());
				}
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
				 logger.info("The Project you selected is:"+comboBox.getSelectedItem());
			 }			
		}
	}
}
