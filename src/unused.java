
public class unused {
	/*if(me.jdbener.Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true") && me.jdbener.Bennerbot.conf.get("enableOutput").toString().equalsIgnoreCase("true") && me.jdbener.Bennerbot.conf.get("enableUserMessages").toString().equalsIgnoreCase("true")){
	/*editor = new JEditorPane("text/html", "<center><h1>Loading...</h1>Please Wait!</center>");
	editor.setEditable(false);
	editor.setEditorKit(new HTMLEditorKit());
	editor.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	field = new JTextField();
	field.setPreferredSize(new Dimension(275, 25));
	field.addActionListener(this);
	button = new JButton("send");
	button.setPreferredSize(new Dimension(25, 25));
	button.addActionListener(this);
	
	pane = new JScrollPane(editor);
	pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	pane.setPreferredSize(new Dimension(315, 525));
	pane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	f = new JFrame(Bennerbot.name+" v"+Bennerbot.version+" ~ Chat");
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setLayout(new BorderLayout());
	f.getContentPane().add(pane, BorderLayout.CENTER);
	f.getContentPane().add(field, BorderLayout.PAGE_END);
	f.getContentPane().add(button, BorderLayout.PAGE_END);
	f.setLayout(new FlowLayout());
	f.setSize(325, 600);
	f.setResizable(false);
	f.setVisible(true);
	new SmartScroller(pane);
	Display display = new Display();
	final int SHELL_TRIM = SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE;
	Shell shell = new Shell(display, SHELL_TRIM & (~SWT.RESIZE));
	
	RowLayout rowLayout = new RowLayout();
	rowLayout.wrap = true;
	rowLayout.pack = false;
	rowLayout.justify = true;
	rowLayout.type = SWT.VERTICAL;
	rowLayout.marginLeft = 5;
	rowLayout.marginTop = 5;
	rowLayout.marginRight = 5;
	rowLayout.marginBottom = 5; 
	rowLayout.spacing = 0;
	shell.setLayout(rowLayout);
	
	shell.setSize(325, 675);
	shell.setText(Bennerbot.name+" v"+Bennerbot.version+" ~ Chat");
	try {
		browser = new Browser(shell, SWT.NONE);
		browser.setLayoutData(new RowData(315, 615));
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	
	text = new Text (shell, SWT.SINGLE | SWT.BORDER);
	text.setLayoutData(new RowData(250, 15));
	text.addListener(SWT.Traverse, new Listener()
    {
        @Override
        public void handleEvent(Event event)
        {
            if(event.detail == SWT.TRAVERSE_RETURN)
            {
                action();
            }
        }

    });
	Button ok = new Button (shell, SWT.PUSH);
	ok.setText ("Send");
	ok.setLayoutData(new RowData(50, 20));
	ok.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			action();
		}
	});
	
	Image small = new Image(display,"resource/BennerBotLogo.png");
    shell.setImage(small);  
	
	shell.setLayout (new RowLayout ());
	//shell.pack ();
	shell.open();
	
	try {
		browser.setUrl(new File("display.html").toURI().toURL().toString());
	} catch (MalformedURLException e) {
		e.printStackTrace();
	}
	String path = "";
	try {
		path = new File("output.html").toURI().toURL().toString();
	} catch (MalformedURLException e1) {
		e1.printStackTrace();
	}
	System.out.println(path);
	String html = 
"<html>"+
"<head>"+
"<title>"+Bennerbot.name+" v"+Bennerbot.version+" ~ Chat</title>"+
"<style>"+
    "html body{"+
        "margin: 0px;"+
        "min-height: 100%;"+
        "height: 100%;"+
        "width: 100%;"+
        "overflow:hidden;"+
    "}"+

    "#load{"+
        "margin: 0px;"+
        "height:100%;"+
        "width: 100%;"+
     "}"+
"</style>"+
"<script type=\"text/javascript\">"+
    "var bottom = false;"+
    "var prev = true;"+
    "function scroll(){"+
        "prev = bottom;"+
        "var myHeight = document.getElementById('load').contentWindow.document.documentElement.clientHeight,"+
            "myScroll = document.getElementById('load').contentWindow.pageYOffset;"+
        
        "if((myHeight + myScroll) >= document.getElementById('load').contentWindow.document.body.offsetHeight) {"+
            "bottom = true;"+
        "}else{"+
            "bottom = false;"+
        "}"+
        "if(prev == true && bottom == false){"+
            "document.getElementById('load').contentWindow.scrollTo( 0, document.getElementById('load').contentWindow.document.body.offsetHeight + 100);"+
            "console.log(\"scroll\");"+
        "}"+
        "document.getElementById('load').src = \""+path+"?s=\"+(myHeight + myScroll);"+
    "}"+
"</script>"+
"</head>"+
"<body>"+
"<iframe src=\""+path+"\" id=\"load\"><iframe>"+
"</body>"+
"</html>";
	//browser.setText(html);
	
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
	System.exit(0);
} else if(me.jdbener.Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true") && me.jdbener.Bennerbot.conf.get("enableOutput").toString().equalsIgnoreCase("true") && !me.jdbener.Bennerbot.conf.get("enableUserMessages").toString().equalsIgnoreCase("true")){
	/*editor = new JEditorPane("text/html", "<center><h1>Loading...</h1>Please Wait!</center>");
	editor.setEditable(false);
	editor.setEditorKit(new HTMLEditorKit());
	editor.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	pane = new JScrollPane(editor);
	pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	pane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	f = new JFrame(Bennerbot.name+" v"+Bennerbot.version+" ~ Chat");
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.getContentPane().add(pane);
	f.setSize(325, 600);
	f.setVisible(true);
	new SmartScroller(pane);
	Display display = new Display();
	Shell shell = new Shell(display);
	
	shell.setLayout(new FillLayout());
	
	shell.setSize(335, 675);
	shell.setText(Bennerbot.name+" v"+Bennerbot.version+" ~ Chat");
	Browser browser;
	try {
		browser = new Browser(shell, SWT.NONE);
		browser.setLayoutData(new RowData(315, 600));
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	
	Image small = new Image(display,"resource/BennerBotLogo.png");
    shell.setImage(small);  
	
	//shell.pack ();
	shell.open();
	try {
		browser.setUrl(new File("output.html").toURI().toURL().toString());
	} catch (MalformedURLException e) {
		e.printStackTrace();
	}
		/*browser.setText("<!DOCTYPE html><html><head><title>BennerBot v0.7 ~ Chat</title><link rel=\"stylesheet\" type=\"text/css\" href=\"resource/layout.css\" media=\"screen\" />"+
						"<script  type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js\"></script>"+
						"<script type=\"text/javascript\">"+
						"var height = 0;function myFunction(){$('#load').load('output.html?_=' +Math.random()).fadeIn(\"slow\"); if(height != $(\"#load\").get(0).scrollHeight){$('html,body').animate({scrollTop : $(\"#load\").get(0).scrollHeight},3000);height = $(\"#load\").get(0).scrollHeight;}};"+
						"</script></head><body onLoad=\"setInterval(function (){myFunction();}, 1000);\">"+
						"<div id=\"load\"> </div></body></html>");
	while (!shell.isDisposed()) {
		//reload();
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
	System.exit(0);
} else {*/
	/*Display display = new Display();
	final int SHELL_TRIM = SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE;
	Shell shell = new Shell(display, SHELL_TRIM & ~(SWT.RESIZE | SWT.MIN | SWT.MAX));
	shell.setSize(190, 45);
	shell.setText(Bennerbot.name+" v"+Bennerbot.version);
	Label label = new Label (shell, SWT.WRAP);
	label.setText("Close this window to stop the bot");
	label.pack();
	Image small = new Image(display,"resource/BennerBotLogo.png");
    shell.setImage(small);  
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep (); 
	} 
	display.dispose ();
	System.exit(0);*/
}
