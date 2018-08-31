var c = document.getElementById("panel");
var cxt = c.getContext("2d");

var CHESS_PANEL_SIZE = 15; // 棋盘大小（如15表示15*15）
var CELL_SIZE = 40; // 棋盘单元格尺寸
var OFFSET_X = 60; // 棋盘在X轴方向的偏移量
var OFFSET_Y = 60; // 棋盘在Y轴方向的偏移量
var STAR_RADIUS_SIZE = 6; // 棋盘
var LINE_COLOR = "#000000"; // 线条颜色
var STAR_COLOR = "#000000"; // 星颜色

var BLACK_CHESS = 1; // 黑子
var WHITE_CHESS = -1; // 白子
var BLACK_CHESS_IMG_NAME = "images/black_chess.png"; // 黑子图片名称
var WHITE_CHESS_IMG_NAME = "images/white_chess.png"; // 白子图片名称
var blackChessImg; // 黑子图片
var whiteChessImg; // 白子图片
var imageWidth = 20; // 棋子图片宽度
var imageHeight = 20; // 棋子图片高度
var step = 0;

var turn = true; // 令牌。用于判断当前轮到黑棋还是白棋。默认黑棋先走，即true表示轮到黑棋走，否则轮到白棋走。
var gameover = false;
var chessTable = new Array(0); // 保存棋盘中的棋子

// 系统初始化
init();

function init(){
	initPanel();

	initArray();

	initChessImages();
	
	initInfo();

	c.addEventListener("mouseup",doMouseDown,false);
	
	paintChesses();
	
	gameover = false;
}

function initInfo(){
	document.getElementById("history").innerHTML = "";
	document.getElementsByName("turn")[0].checked = true;
}

/**
 * 初始化棋盘
 */
function initPanel() {
	clearCanvas();
	drawLines();
	drawStars();
	drawValues();
}

function clearCanvas(){
	cxt.clearRect(0,0,c.width,c.height);
}

/**
 * 绘制棋盘中的横线、竖线
 */
function drawLines() {
	// 缓存线条颜色
	var oldStrokeStyle = cxt.strokeStyle;
	
	// 设置线条颜色
	cxt.strokeStyle = LINE_COLOR;
	
	for(var i=0;i<CHESS_PANEL_SIZE;i++) {
		// 绘制棋盘中的横线
		cxt.moveTo(OFFSET_X, OFFSET_Y + CELL_SIZE * i);
		cxt.lineTo(OFFSET_X + CELL_SIZE * (CHESS_PANEL_SIZE - 1), 
			OFFSET_Y + CELL_SIZE * i);
		cxt.stroke();
		
		// 绘制棋盘中的竖线
		cxt.moveTo(OFFSET_X + CELL_SIZE * i, OFFSET_Y);
		cxt.lineTo(OFFSET_X + CELL_SIZE * i, 
			OFFSET_Y + CELL_SIZE * (CHESS_PANEL_SIZE - 1));
		cxt.stroke();
	}
	cxt.stroke();
	
	// 恢复线条颜色
	cxt.strokeStyle = oldStrokeStyle;
}

/**
 * 画棋盘中的星
 */
function drawStars() {
	// 缓存填充颜色
	var oldFillStyle = cxt.fillStyle;
	
	// 设置星颜色
	cxt.fillStyle = STAR_COLOR;
	
	// 画星
	drawStar(OFFSET_X + CELL_SIZE * 3, OFFSET_Y + CELL_SIZE * 3, STAR_RADIUS_SIZE);
	drawStar(OFFSET_X + CELL_SIZE * 3, OFFSET_Y + CELL_SIZE * 11, STAR_RADIUS_SIZE);
	drawStar(OFFSET_X + CELL_SIZE * 11, OFFSET_Y + CELL_SIZE * 3, STAR_RADIUS_SIZE);
	drawStar(OFFSET_X + CELL_SIZE * 11, OFFSET_Y + CELL_SIZE * 11, STAR_RADIUS_SIZE);
	drawStar(OFFSET_X + CELL_SIZE * 7, OFFSET_Y + CELL_SIZE * 7, STAR_RADIUS_SIZE);
	
	// 恢复填充颜色
	cxt.fillStyle = oldFillStyle;
}

/**
 * 画星<br/> 以坐标(x, y)为圆心，radius为半径
 * @param x 圆心的x坐标
 * @param y 圆心的y坐标
 * @param radius 星的半径
 */
function drawStar(x, y, radius) {
	cxt.beginPath();
	cxt.arc(x, y, radius, 0, Math.PI * 2, true);
	cxt.closePath();
	cxt.fill();
}

/**
 * 画棋盘的辅助坐标值
 * 
 * @returns
 */
function drawValues(){
	for(var i=1;i<=15;i++) {
	    cxt.font = "20px Arial";
	    cxt.fillStyle = "black";
	    cxt.fillText("" + i, 10, 25 + i * CELL_SIZE);
	    cxt.fillText(String.fromCharCode(96 + i), 15 + i * CELL_SIZE, 20);
	}
}

/**
 * 初始化棋子图片
 */
function initChessImages() {
	blackChessImg = new Image(); // 黑子图片
	blackChessImg.src = BLACK_CHESS_IMG_NAME;
	
	whiteChessImg = new Image(); // 白子图片
	whiteChessImg.src = WHITE_CHESS_IMG_NAME;
}

/**
 * 初始化数组，用于存储棋盘上的棋子信息
 */
function initArray() {
	chessTable = new Array();
	for(var i = 0; i < CHESS_PANEL_SIZE; i ++) {
		chessTable[i] = new Array();
		for(var j = 0; j < CHESS_PANEL_SIZE; j ++) {
			chessTable[i][j] = 0;
		}
	}
}

/**
 * 绘制棋盘中的棋子
 */
function paintChesses() {
	for(var i=0;i<chessTable.length;i++) {
		for(var j=0;j<chessTable[i].length;j++) {
			switch(chessTable[i][j]) {
			case 1:
				drawImage(blackChessImg, i, j);
				break;
			case -1:
				drawImage(whiteChessImg, i, j);
				break;
			default:
				
			}
		}
	}
}

/**
 * 绘制图片
 * @param img 需要绘制的图片对象
 * @param offsetX 图片的相对x坐标
 * @param offsetY 图片的相对y坐标
 */
function drawImage(img, offsetX, offsetY) {
	var x = OFFSET_X + CELL_SIZE * offsetX - imageWidth;
	var y = OFFSET_Y + CELL_SIZE * offsetY - imageHeight;
	cxt.drawImage(img, x, y);
}

/**
 * 
 */
function doMouseDown(e) {
	if(gameover){
		if(confirm("再来一局？")){
			init();
		}
		return;
	}
	var p = getRelativePointOnCanvas(c, e.pageX, e.pageY);
	if(p != null && chessTable[p.x][p.y] == 0) {
		var li = document.createElement('li');
	    li.innerText = (turn ? "B" : "W") + toString(p);
	    document.getElementById("history").appendChild(li);
	    
		if(turn) {
			chessTable[p.x][p.y] = BLACK_CHESS;
			document.getElementsByName("turn")[1].checked = true;
		} else {
			chessTable[p.x][p.y] = WHITE_CHESS;
			document.getElementsByName("turn")[0].checked = true;
		}
		paintChesses();
		
		var win = isWin(p.x, p.y);
//		console.log("isWin: " + p.x + ", " + p.y + " " + win);
		if(win){
			gameover = true;
			alert("恭喜" + (turn ? "黑棋" : "白棋") + "获得胜利！");
			return;
		}
		
		turn = !turn;
	}
}

function toString(p) {
	return String.fromCharCode(97 + p.x) + (p.y + 1);
}

/**
 * 返回鼠标在Canvas的坐标
 * @param canvas Canvas对象
 * @param x 鼠标基于整个屏幕的横坐标
 * @param y 鼠标基于整个屏幕的纵坐标
 * @return 坐标{x:v_x, y:v_y}
 */
function getAbsolutePointOnCanvas(canvas, x, y) {
	// 获取Canvas对象相对屏幕的位置
	var box = canvas.getBoundingClientRect();
	var canvasX = x - box.left * (canvas.width / box.width);
	var canvasY = y - box.top  * (canvas.height / box.height);
	return {x: canvasX, y: canvasY};
}

/**
 * 以CELL_SIZE为参照，返回(x, y)在Canvas的相对坐标<br/>
 * @param canvas Canvas对象
 * @param x 鼠标基于整个屏幕的横坐标
 * @param y 鼠标基于整个屏幕的纵坐标
 * @return 相对坐标{x:v_x, y:v_y}(取值范围：[0, CHESS_PANEL_SIZE-1])或返回null
 */
function getRelativePointOnCanvas(canvas, x, y) {
	var p = getAbsolutePointOnCanvas(canvas, x, y);
	
	var canvasX = p.x;
	var canvasY = p.y;
	
	//防止左溢出或上溢出
	if(canvasX < OFFSET_X / 2 || canvasX > OFFSET_X + CELL_SIZE * (CHESS_PANEL_SIZE - 1) + OFFSET_X / 2) {
		return null;
	}
	if(canvasY < OFFSET_Y / 2 || canvasY > OFFSET_Y + CELL_SIZE * (CHESS_PANEL_SIZE - 1) + OFFSET_Y / 2) {
		return null;
	}
	
	var rx = Math.floor((canvasX - OFFSET_X + CELL_SIZE / 2) / CELL_SIZE);
	if(rx < 0) {
		rx = 0;
	}
	var ry = Math.floor((canvasY - OFFSET_Y + CELL_SIZE / 2) / CELL_SIZE);
	if(ry < 0) {
		ry = 0;
	}
	
//	防止右溢出或下溢出
	if(rx >= 15 || ry >= 15){
		return null;
	}
	return {x: rx, y: ry};
}

/**
 * 判断当前是否赢了
 */
function isWin(x, y) {
	return hasFiveInHorizontalDirection(x, y) || hasFiveInVerticalDirection(x, y) || hasFiveInCastAwayDirection(x, y) || hasFiveInShortPausingDirection(x, y);
}

/**
 * 水平方向
 */
function hasFiveInHorizontalDirection(x, y)
{
	var num = 0;
	var i = 0;
	var chess = turn ? BLACK_CHESS : WHITE_CHESS;
	
	while(i < CHESS_PANEL_SIZE) {
		if(chessTable[i][y] == chess) {
			num ++;
		} else {
			num = 0;
		}
		
		if(num >= 5) {
//			console.log("hasFiveInHorizontalDirection(): true.");
			return true;
		}
		
		i++;
	}
	return false;
}

/**
 * 竖直方向
 */
function hasFiveInVerticalDirection(x, y)
{
	var num = 0;
	var i = 0;
	var chess = turn ? BLACK_CHESS : WHITE_CHESS;
	
	while(i < CHESS_PANEL_SIZE) {
		if(chessTable[x][i] == chess) {
			num ++;
		} else {
			num = 0;
		}
		
		if(num >= 5) {
//			console.log("hasFiveInVerticalDirection(): true.");
			return true;
		}
		
		i++;
	}
	
	return false;
}

/**
 * 撇方向
 */
function hasFiveInCastAwayDirection(x, y) {
	var num = 0;
	var chess = turn ? BLACK_CHESS : WHITE_CHESS;
	
	var i = x;
	var j = y;
	while(chessTable[i][j] == chess){
		num ++;
		
		i -= 1;
		j += 1;
		if(i < 0 || j > 14){
			break;
		}
	}
	
	i = x;
	j = y;
	while(chessTable[i][j] == chess) {
		num ++;
		
		i += 1;
		j -= 1;
		if(i > 14 || j < 0){
			break;
		}
	}
	
	if(num - 1 >= 5){
//		console.log("hasFiveInCastAwayDirection(): true.");
		return true;
	}
	
	return false;
}

/**
 * 捺方向
 */
function hasFiveInShortPausingDirection(x, y) {
	var num = 0;
	var i = 0;
	var chess = turn ? BLACK_CHESS : WHITE_CHESS;

	var i = x;
	var j = y;
	while(chessTable[i][j] == chess){
		num ++;
		
		i -= 1;
		j -= 1;
		if(i < 0 || j < 0){
			break;
		}
	}
	
	i = x;
	j = y;
	while(chessTable[i][j] == chess) {
		num ++;
		
		i += 1;
		j += 1;
		if(i > 14 || j > 14){
			break;
		}
	}
	
	if(num - 1 >= 5){
//		console.log("hasFiveInShortPausingDirection(): true.");
		return true;
	}
	
	return false;
}