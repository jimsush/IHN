var envmap=['images/room.jpg','images/room.jpg','images/room.jpg','images/room.jpg','images/room.jpg','images/room.jpg'];
var bidFinder;

function setup(htmlElementId){				
	var network= new mono.Network3D();
	network.setClearColor('#39609B');
	//network.setShowFps(true);

	var camera = new mono.PerspectiveCamera(30, 1.5, 30, 30000);
	camera.setPosition(0,5000,8000);
	network.setCamera(camera);
	
	var interaction = network.getDefaultInteraction();
	interaction.yLowerLimitAngle=Math.PI/180*2;
	interaction.yUpLimitAngle=Math.PI/2;
	interaction.maxDistance=15000;
	interaction.minDistance=50;
	interaction.zoomSpeed=1;
	interaction.panSpeed=0.2;

	bidFinder = new mono.QuickFinder(network.getDataBox(), 'bid', 'client');
	network.isSelectable = function(element){return false;};		
	document.getElementById(htmlElementId).appendChild(network.getRootView());
	mono.Utils.autoAdjustNetworkBounds(network,document.documentElement,'clientWidth','clientHeight');
	network.getRootView().addEventListener('dblclick', function(e){
		handleDoubleClick(e, network);
	});	

	setupLights(network.getDataBox());

	loadData(network.getDataBox());	
}

function loadData(box){
	createCar(box, -880, 70, -2290, Math.PI/180*5);
	createFloor(box, 5000, 0, 5400);
	createFloor(box, 1000, 330, 2000).setPosition(2995, 190, -1655);
	var floor01 = createFloor(box, 500, 330, 2000);
	floor01.s({'m.transparent':true,'m.opacity':0.5}).setPosition(3255, 590, -1655);
	floor01.setClient('bid', 'floor01');
	createSpot(box, 0, -600, false, 'A0', 10);
	createSpot(box, 0, -1300, true, 'B0', 10);
	createSpot(box, 0, -2300, false, 'C0', 44);
	createPole(box);
	createWalls(box);
	createShop(box);
	createTrail(box);
	createWorker(box,1000, 2650, Math.PI/2);	
	createStair(box);
	createPlant(box, 2200, -2300);
	createPlant(box, 2300, -2300);
	createPlant(box, 2400, -2300);	
	createDoor(box, null, 3100, 205, -1800, 'split', 'metal').rotateFromAxis(new mono.Vec3(0, 1, 0), new mono.Vec3(3000, 0, -2300), Math.PI/2);
	createDoor(box, null, 3100, 605, -1800, 'split', 'metal').rotateFromAxis(new mono.Vec3(0, 1, 0), new mono.Vec3(3000, 0, -2300), Math.PI/2);
	createLift(box);
	createLine(box);	
}

function createLine(box){
	var path=new mono.Path();
	path.moveTo(-580,0,-2180);
	path.lineTo(-500,0,-1800);
	path.lineTo(1600,0,-1800);
	path.lineTo(1600,0,-2500);
	path.lineTo(1950,0,-2500);
	path.lineTo(2350,210,-2500);
	path.lineTo(3630,210,-2400);
	path.lineTo(3630,600,-2400);
	path.lineTo(3130,600,-2400);
	path.lineTo(3130,600,-1200);

	path = mono.PathNode.prototype.adjustPath(path, 100);

	var cable=new mono.PathNode(path, 100, 10);
	cable.s({
		'm.type': 'phong',
		'm.specularStrength': 30,
		'm.color': '#B43104',
		'm.ambient': '#B43104',
		'm.texture.repeat': new mono.Vec2(150, 1),
		'm.texture.image': 'images/wall.jpg',
		'm.visible': false
	});
	cable.setStartCap('plain');
	cable.setEndCap('plain');
	cable.setPositionY(20);
	cable.setClient('bid', 'cable01');
	box.add(cable);

	var pin=new mono.Billboard();
	pin.s({
		'm.texture.image': 'images/pin.png',		
		'm.alignment': mono.BillboardAlignment.bottomCenter,
		'm.transparent': true,
	});
	pin.setScale(200,0.1,1);
	pin.setPosition(3130,600,-1200);
	pin.setClient('bid','pin02');
	box.add(pin);
}

function createLift(box){
	var cube=new mono.Cube(200, 1500, 200);
	cube.setPosition(3610, cube.getHeight()/2, -2400);
	cube.s({
		//'m.type': 'phong',
		'm.side': mono.DoubleSide,
		//'m.color': '#F3E2A9',
		//'m.ambient': '#F3E2A9',
		//'m.lightmap.image': 'images/outside_lightmap.jpg',
		'm.texture.image': 'images/grid.png',
		'left.texture.visible': false,
		'm.transparent': true,
		'm.texture.repeat': new mono.Vec2(2,10),
		//'m.opacity': 0.5,
		//'m.specularStrength': 0.01,
		//'m.envmap.image': envmap,
	});
	box.add(cube);

	cube=new mono.Cube(170, 250, 170);
	cube.setPosition(3610, cube.getHeight()/2+800, -2400);
	cube.s({
		//'m.type': 'phong',
		'm.side': mono.DoubleSide,
		//'m.color': '#F3E2A9',
		//'m.ambient': '#F3E2A9',
		'm.lightmap.image': 'images/outside_lightmap.jpg',
		'm.texture.image': 'images/metal.jpg',
		'left.texture.visible': false,
		//'m.transparent': true,
		//'m.opacity': 0.5,
		//'m.specularStrength': 0.01,
		//'m.envmap.image': envmap,
	});
	box.add(cube);
}

function createPlant(box, x, z){
	var w=30;
	var h=120;
	var pic='images/plant.png';
	var objects=[];

	var cylinderVase=new mono.Cylinder(w*0.6,w*0.4,h/5,20,1,false,false);
	cylinderVase.s({
		'm.type': 'phong',
		'm.color': '#845527',
		'm.ambient': '#845527',
		'm.texture.repeat': new mono.Vec2(10,4),
		'm.specularmap.image': 'images/metal_normalmap.jpg',
		'm.normalmap.image':'images/metal_normalmap.jpg',
	});			
	var cylinderHollow=cylinderVase.clone();
	cylinderHollow.setScale(0.9,1,0.9);
	var cylinderMud=cylinderHollow.clone();
	cylinderMud.setScale(0.9,0.7,0.9);
	cylinderMud.s({
		'm.type': 'phong',
		'm.color': '#163511',
		'm.ambient': '#163511',
		'm.texture.repeat': new mono.Vec2(10,4),
		'm.specularmap.image': 'images/metal_normalmap.jpg',
		'm.normalmap.image':'images/metal_normalmap.jpg',
	});
	var vase=new mono.ComboNode([cylinderVase,cylinderHollow,cylinderMud],['-','+']);
	objects.push(vase);

	var count=5;
	for(var i=0;i<count;i++){
		var plant=new mono.Cube(w*2,h,0.01);	

		plant.s({
			'm.visible': false,
			'm.alphaTest': 0.5,
			'front.m.visible': true,
			'front.m.texture.image': pic,
			'back.m.visible': true,
			'back.m.texture.image': pic,
		});
		plant.setParent(vase);
		plant.setPositionY(cylinderVase.getHeight()/2+plant.getHeight()/2-3);
		plant.setRotationY(Math.PI * i/count);
		objects.push(plant);
	}

	var plant=new mono.ComboNode(objects);				
	plant.setPosition(x, cylinderVase.getHeight()/2, z);
	box.add(plant);
	return plant;
}

function createStair(box){
	var parts=[];
	var ops=[];
	var count=20;
	for(var i=0;i<count;i++){
		var cube=new mono.Cube(300,10,500-20*i);
		cube.setPosition(0, i*10, 20*(count-i)/2);
		cube.s({
			'm.type': 'phong',
			'm.color': '#AABAC9',//'#BEC9BE',
			'm.ambient': '#AABAC9',//'#BEC9BE',
			'm.texture.image': 'images/floor.jpg',
			'm.texture.repeat': new mono.Vec2(cube.getWidth()/250*2,cube.getHeight()/500*2),
			'top.m.texture.repeat': new mono.Vec2(cube.getWidth()/250*2,cube.getDepth()/500*2),
		});
		parts.push(cube);
		ops.push('+');
	}
	var stair=new mono.ComboNode(parts, ops);
	stair.setPosition(2440, 10, -2490);
	stair.setRotationY(-Math.PI/2);
	box.add(stair);
}

function createTrail(box){
	var points=[[1000, 2700], [1000, 2500],[-2000, 2500],[-2000,-1800],[-600,-1800],[-600,-2200]];

	var path=new mono.Path();
	path.moveTo(points[0][0], points[0][1]);
	for(var i=1;i<points.length; i++){
		path.lineTo(points[i][0], points[i][1]);
	}
	path = mono.PathNode.prototype.adjustPath(path, 150);

	var trail=new mono.PathCube(path, 40, 10);
	trail.s({
		'm.type': 'phong',
		'm.specularStrength': 30,
		'm.color': '#DF7401',
		'm.ambient': '#DF7401', 
		'm.visible': false		
	});
	trail.setRotationX(Math.PI);
	trail.setPositionY(5);
	trail.setClient('bid','trail01');
	box.add(trail);

	var pin=new mono.Billboard();
	pin.s({
		'm.texture.image': 'images/pin.png',		
		'm.alignment': mono.BillboardAlignment.bottomCenter,
		'm.transparent': true,
	});
	pin.setScale(200,0.1,1);
	pin.setPosition(-600, 0, -2200);
	pin.setClient('bid','pin01');
	box.add(pin);
}

function createWorker(box, x, z, rotation){
	var obj='images/worker.obj';
	var mtl='images/worker.mtl';               
		
	var loader = new mono.OBJMTLLoader();
	loader.load(obj, mtl, {'worker': 'images/worker.png',}, function (object) {                    			
		object.setScale(3,3,3);		
		object.setPosition(x,0,z);
		object.setRotationY(rotation);
		object.setClient('type', 'person');	
		box.addByDescendant(object);
		
		var updater=function(element){
			if(element && element.getChildren()){
				element.getChildren().forEach(function(child){
					child.setStyle('m.normalType', mono.NormalTypeSmooth);
					updater(child);
				});
			}
		}
		updater(object);
	});
}

function createCar(box, x, y, z, rotation){
	var obj='car/car.obj';
	var mtl='car/car.mtl';               
		
	var loader = new mono.OBJMTLLoader();
	var pics={
		'carbodyD': 'car/carbodyD.png',
		'carbodyS': 'car/carbodyS.png',
		'glass': 'car/glass.png',
		'WheelD': 'car/WheelD.png',
		'WheelN': 'car/WheelN.png',
	};
	loader.load(obj, mtl, pics, function (object) {                    			
		object.setPosition(x,y,z);
		object.setRotationY(rotation);
		box.addByDescendant(object);		

		var updater=function(element){
			if(element && element.getChildren()){
				element.getChildren().forEach(function(child){
					child.s({
						//'m.type': 'phong',
						'm.normalType': mono.NormalTypeSmooth,
						//'m.color': 'red',
					});
					updater(child);
				});
			}
		}
		updater(object);
	});
}

function createShop(box){
	var path=new mono.Path();
 
	path.moveTo(0,0);
	path.lineTo(1960,0);
	path.lineTo(1960,260);
	path.lineTo(1460, 260);
	path.lineTo(1460, 760);
	path.lineTo(0, 760);
	path.lineTo(0,0);

	var node=new mono.ShapeNode(path, 1, 220, true, 200);
	node.s({
		'm.type': 'phong',
		'm.color': '#D7DF01',
		'm.ambient': '#D7DF01',
		'm.transparent': true,
		'm.opacity': 0.5,
		'm.specularStrength': 2,
		'm.normalmap.image': 'images/normalmap.jpg',
	});
	node.setPositionX(-1480);
	node.setPositionZ(1280);
	box.add(node);

	var pin=new mono.Billboard();
	pin.s({
		'm.texture.image': 'images/logo1.png',		
		'm.alignment': mono.BillboardAlignment.bottomCenter,
		'm.transparent': true,
	});
	pin.setScale(200,200,1);
	pin.setParent(node); 
	pin.setPositionX(800);
	pin.setPositionY(node.getAmount());
	pin.setPositionZ(-500);
	box.add(pin); 

	//another shop.
	var path=new mono.Path();
 
	path.moveTo(0,0);
	path.lineTo(540,0);
	path.lineTo(540,960);
	path.lineTo(0, 960);
	path.lineTo(0, 0);

	var node=new mono.ShapeNode(path, 1, 220, true, 200);
	node.s({
		'm.type': 'phong',
		'm.color': '#04B45F',
		'm.ambient': '#04B45F',
		'm.transparent': true,
		'm.opacity': 0.5,
		'm.specularStrength': 2,
		'm.normalmap.image': 'images/normalmap.jpg',
	});
	node.setPosition(930, 0, 2280);
	box.add(node);

	var pin=new mono.Billboard();
	pin.s({
		'm.texture.image': 'images/logo2.png',		
		'm.alignment': mono.BillboardAlignment.bottomCenter,
		'm.transparent': true,
	});
	pin.setScale(200,200,1);
	pin.setParent(node); 
	pin.setPosition(300, node.getAmount(), -500);
	box.add(pin); 
}

function createWalls(box){
	createWallFloor(box, 3000, 2000, 0, 0, 1300);
	createWall(box, [[-1000, 4950], [4000, 4950], [4000, 2950]]);
	createWall(box, [[0, 0],[3000, 0],[3000, 2000],[0, 2000],[0,0]], false, {x: 650, z: 2300});
	createWall(box, [[0, 1000],[3000, 1000]], true);
	createWall(box, [[400, 0],[400, 1000]], true);
	createWall(box, [[700, 0],[700, 1000]], true);
	createWall(box, [[1200, 0],[1200, 1000]], true);
	createWall(box, [[1500, 0],[1500, 1000]], true);
	createWall(box, [[1900, 0],[1900, 1000]], true);
	createWall(box, [[2400, 0],[2400, 1000]], true);
	createWall(box, [[0, 500],[100, 500]], true);
	createWall(box, [[0, 1800],[1500, 1800],[1500, 1300],[2000,1300],[2000,1000]], true);

	createWall(box, [[3990, 4950], [5000, 4950],[5000,4800]]).s({'m.transparent':true,'m.opacity':0.5}).setPositionY(180);
	createWall(box, [[5000,4600],[5000,2950],[4500,2950]]).s({'m.transparent':true,'m.opacity':0.5}).setPositionY(180);
	
	createWall(box, [[4500, 4950], [5000, 4950],[5000,4800]]).s({'m.transparent':true,'m.opacity':0.5}).setPositionY(580);
	createWall(box, [[5000,4600],[5000,2950],[4500,2950]]).s({'m.transparent':true,'m.opacity':0.5}).setPositionY(580);
}

function createWall(box, data, inside, door){
	var path=new mono.Path();
	path.moveTo(data[0][0], data[0][1], 0);
	for(var i=1;i<data.length;i++){
		path.lineTo(data[i][0], data[i][1], 0);
	}

	var thick=20, height=200;
	var outsideColor= inside ? '#B8CAD5' : '#CEE3F6', insideColor='#B8CAD5', topColor='#D6E4EC';
	var wall= new mono.PathCube(path, thick, height, 1, 100);
	wall.s({
		'outside.m.color': outsideColor,
		'inside.m.type': 'basic',
		'inside.m.color': insideColor,
		'aside.m.color': outsideColor,
		'zside.m.color': outsideColor,
		'top.m.color': topColor,
		'bottom.m.color': topColor,
		'inside.m.lightmap.image': 'images/inside_lightmap.jpg',
		'outside.m.lightmap.image': 'images/outside_lightmap.jpg',
		'aside.m.lightmap.image': 'images/outside_lightmap.jpg',
		'zside.m.lightmap.image': 'images/outside_lightmap.jpg',
	});
	if(!inside){
		wall.s({
			'outside.m.texture.image': 'images/wall.jpg',
		});
	}
	wall.setPosition(-1500, 0, 2300);

	if(door){
		createDoor(box, wall, door.x, 0, door.z);
	}else{
		box.add(wall);
	}
	return wall;
}

function createFloor(box, width, y, height){	
	var floor=new mono.Cube(width, 30, height);
	floor.setPositionY(-floor.getHeight()/2+y);
	floor.s({
		'm.type': 'phong',
		'm.color': '#CEE3F6',//'#BEC9BE',
		'm.ambient': '#CEE3F6',//'#BEC9BE',
		'top.m.type':'basic',
		'top.m.texture.image': 'images/floor.jpg',
		'top.m.texture.repeat': new mono.Vec2(width/250*2,height/500*2),
		//'top.m.color': '#DAF0F5',
		'top.m.polygonOffset':true,
		'top.m.polygonOffsetFactor':3,
		'top.m.polygonOffsetUnits':3,
	});
	box.add(floor);
	return floor;
}

function createWallFloor(box, width, height, x, y, z){
	var floor=new mono.Cube(width, 1, height);
	//floor.setPositionY(-floor.getHeight()/2);
	floor.s({
		'm.type': 'phong',
		'm.color': '#BEC9BE',
		'm.ambient': '#BEC9BE',
		'top.m.type':'basic',
		'top.m.texture.image': 'images/floor.jpg',
		'top.m.texture.repeat': new mono.Vec2(width/250*2,height/500*2),
		//'top.m.color': '#DAF0F5',
		'top.m.polygonOffset':true,
		'top.m.polygonOffsetFactor':3,
		'top.m.polygonOffsetUnits':3,
	});
	floor.setPosition(x, y, z);
	box.add(floor);
}

function createSpot(box, x, z, reversed, prefix, id){
	var cube=new mono.Cube(2600, 1, 600);
	cube.s({
		'm.type': 'phong',
		'm.color': '#0B6138',
		'm.ambient': '#0B6138',
		'm.specularStrength': 20,
	});
	var border=20;
	var cubeBorder=new mono.Cube(cube.getWidth()+border*2, 1, cube.getDepth()+border*2);
	cubeBorder.s({
		'm.color': '#E4F6F6',
	});	
	var area=new mono.ComboNode([cubeBorder, cube, cube], ['-', '+']);
	area.setPosition(x, 0, z);
	if(reversed){
		area.setRotationY(Math.PI);
	}
	box.add(area);

	var width=250;
	var height=500;

	for(var i=0;i<10;i++){
		var spot=new mono.Cube(width, 1, height);
		spot.s({
			'm.visible': false,
		});
		spot.s({
			'm.type':'basic',
			'm.transparent': true,
			'top.m.visible': true,
			'top.m.texture.image': 'images/spot.png',		
		});
		spot.setPosition(i*250-1100, 1, 0);
		spot.setParent(area);
		spot.setClient('bid', prefix+(id+i));
		box.add(spot);

		var block=new mono.Cylinder(5, 5, width * 0.6);
		block.s({
			'm.type':'phong',
			'm.color': '#D39C1A',
			'm.ambient': '#D39C1A',
			'side.m.texture.image': 'images/block.png',		
			'side.m.texture.repeat': new mono.Vec2(1, 3),
			'top.m.color': 'black',
			'bottom.m.color': 'black',
			'm.specularStrength': 20,
		});
		block.setParent(spot);
		block.setRotationZ(Math.PI/2);
		block.setPositionY(5);//block.getHeight());
		block.setPositionZ(-215);
		box.add(block);

		var label=new mono.Cube(150,2,75);		
		var canvas=generateAssetImage(prefix+(id+i), '#E6E6E6');
		label.setStyle('m.visible', false);
		label.s({
			'top.m.visible': true,
			'top.m.texture.image': canvas,
			//'top.m.specularmap.image': canvas,
			'm.transparent': true,
			'm.texture.anisotropy': 32,
		});
		label.setPositionZ(200);
		label.setParent(spot);
		box.add(label);
	}

	return area;
}

function createPoleObject(box, x, z){
	var cube=new mono.Cube(45, 70, 45);
	cube.s({
		'm.type': 'phong',
		'm.texture.image': 'images/pole.png',
	});
	cube.s({
		'top.m.texture.image': '',
		'top.m.texture.image': 'images/floor.jpg',
		'top.m.color': '#E6EEF6',//'#BEC9BE',
		'top.m.ambient': '#E6EEF6',//'#BEC9BE',
	});
	cube.setPositionY(cube.getHeight()/2);
	//box.add(cube);

	var cube2=new mono.Cube(40, 300, 40);
	cube2.s({
		'm.type': 'phong',
		'm.texture.image': 'images/floor.jpg',
		'm.color': '#E6EEF6',//'#BEC9BE',
		'm.ambient': '#E6EEF6',//'#BEC9BE',
		'm.texture.repeat': new mono.Vec2(1, 5),
	});
	cube2.setPositionY(cube2.getHeight()/2);
	
	var pole=new mono.ComboNode([cube, cube2], ['+']);
	pole.setPositionX(x);
	pole.setPositionZ(z);
	box.add(pole);
}

function createPole(box){
	for(var i=0;i<3;i++){
		createPoleObject(box, -1280+i*1300, -2600);
		createPoleObject(box, -1280+i*1300, -1600);
		createPoleObject(box, -1280+i*1300, -950);
		createPoleObject(box, -1280+i*1300, -320);
	}
}

function generateAssetImage(text, textColor, fillColor){         
	var width=512, height=256;

	var canvas = document.createElement('canvas');
	canvas.width  = width;
	canvas.height = height;

	var ctx = canvas.getContext('2d');		
	if(fillColor){
		ctx.fillStyle=fillColor
		ctx.fillRect(0,0,width,height);
	}
	
	ctx.font = 150+'px "Microsoft Yahei" normal';
	ctx.fillStyle = textColor || 'black';
	ctx.textAlign = 'center';
	ctx.textBaseline = 'middle';
	//ctx.fillText(text, width/2,height/2);
	ctx.strokeStyle=textColor || 'black';
	ctx.lineWidth=15;
	ctx.strokeText(text, width/2,height/2);

	return canvas;   
}

function handleDoubleClick(e, network){
	var camera=network.getCamera();
	var interaction=network.getDefaultInteraction();
	var firstClickObject=findFirstObjectByMouse(network,e);
	if(firstClickObject){
		var element=firstClickObject.element;					
		var newTarget=firstClickObject.point;
		var oldTarget=camera.getTarget();
		
		if(element.getStyle('lazy.function')){
			var loader=element.getStyle('lazy.function');
			var time1=new Date().getTime();
			loader();
			var time2=new Date().getTime();
		}
		if(element.getClient('bid') && element.getClient('bid') == 'C046'){
			var pin01 = bidFinder.findFirst('pin01');
			var trail01 = bidFinder.findFirst('trail01');
			new twaver.Animate({
				from: 0,
				to: 1,
				dur: 2000,
				easing: 'bounceOut',
				onUpdate: function(value){
					pin01.setScaleY(value*200);
				},
				onDone: function(){
					trail01.setStyle('m.visible', true);
				}

			}).play();
		}else if(element.getClient('bid') && element.getClient('bid') == 'floor01'){
			var pin02 = bidFinder.findFirst('pin02');
			var cable01 = bidFinder.findFirst('cable01');
			new twaver.Animate({
				from: 0,
				to: 1,
				dur: 2000,
				easing: 'bounceOut',
				onUpdate: function(value){
					pin02.setScaleY(value*200);
				},
				onDone: function(){
					cable01.setStyle('m.visible', true);
				}

			}).play();
		}else{
			animateCamera(camera, interaction, oldTarget, newTarget, function(){
				if(element.getClient('animation')){
					playAnimation(element, element.getClient('animation'));
				}				
			});
		}
	}else{
		var oldTarget=camera.getTarget();
		var newTarget=new mono.Vec3(0,0,0);
		animateCamera(camera, interaction, oldTarget, newTarget);
	}
}

function setupLights(box){
	var pointLight = new mono.PointLight(0xFFFFFF,0.3);
	pointLight.setPosition(0,1000,-1000);
	box.add(pointLight);     
	
	var pointLight = new mono.PointLight(0xFFFFFF,0.3);
	pointLight.setPosition(0,1000,1000);
	box.add(pointLight);        

	var pointLight = new mono.PointLight(0xFFFFFF,0.3);
	pointLight.setPosition(1000,-1000,-1000);
	box.add(pointLight);        

	box.add(new mono.AmbientLight('white'));	
}

function findFirstObjectByMouse(network, e){
	var objects = network.getElementsByMouseEvent(e);
	if (objects.length) {
		for(var i=0;i<objects.length;i++){			
			var first = objects[i];
			var object3d = first.element;
			if(! (object3d instanceof mono.Billboard)){
				return first;
			}
		}
	}
	return null;
}

function animateCamera(camera, interaction, oldPoint, newPoint, onDone){
	twaver.Util.stopAllAnimates(true);
	
	var offset=camera.getPosition().sub(camera.getTarget());
	var animation=new twaver.Animate({
		from: 0,
		to: 1,
		dur: 500,
		easing: 'easeBoth',
		onUpdate: function (value) {
			var x=oldPoint.x+(newPoint.x-oldPoint.x)*value;
			var y=oldPoint.y+(newPoint.y-oldPoint.y)*value;
			var z=oldPoint.z+(newPoint.z-oldPoint.z)*value;
			var target=new mono.Vec3(x,y,z);				
			camera.lookAt(target);
			interaction.target=target;
			var position=new mono.Vec3().addVectors(offset, target);
			camera.setPosition(position);
		},
	});
	animation.onDone=onDone;
	animation.play();
}

function playAnimation(element, animation){
	var params=animation.split('.');
	if(params[0]==='move'){
		var direction=params[1];
		animateMove(element, direction);
	}
	if(params[0]==='rotate'){
		var anchor=params[1];
		var angle=params[2];
		animateRotate(element, anchor, angle);
	}
}

function animateMove(object, direction){
	twaver.Util.stopAllAnimates(true);

	var size=object.getBoundingBox().size().multiply(object.getScale());

	var movement=0.8;
	
	var directionVec=new mono.Vec3(0, 0, 1);
	var distance=0;
	if(direction==='x'){
		directionVec=new mono.Vec3(1, 0, 0);
		distance=size.x;
	}
	if(direction==='-x'){
		directionVec=new mono.Vec3(-1, 0, 0);
		distance=size.x;
	}
	if(direction==='y'){
		directionVec=new mono.Vec3(0, 1, 0);
		distance=size.y;
	}
	if(direction==='-y'){
		directionVec=new mono.Vec3(0, -1, 0);
		distance=size.y;
	}
	if(direction==='z'){
		directionVec=new mono.Vec3(0, 0, 1);
		distance=size.z;
	}
	if(direction==='-z'){
		directionVec=new mono.Vec3(0, 0, -1);
		distance=size.z;
	}

	distance=distance*movement;
	if(object.getClient('animated')){
		directionVec=directionVec.negate();
	}

	var fromPosition=object.getPosition().clone();		
	object.setClient('animated', !object.getClient('animated'));

	new twaver.Animate({
		from: 0,
		to: 1,
		dur: 2000,
		easing: 'bounceOut',
		onUpdate: function (value) {
			//don't forget to clone new instance before use them!
			object.setPosition(fromPosition.clone().add(directionVec.clone().multiplyScalar(distance * value)));
		},
	}).play();
}

function animateRotate(object, anchor, angle){
	twaver.Util.stopAllAnimates(true);

	var size=object.getBoundingBox().size().multiply(object.getScale());
	
	var from=0;
	var to=1;
	if(object.getClient('animated')){
		to=-1;
	}
	object.setClient('animated', !object.getClient('animated'));
	
	var position;
	var axis;
	if(anchor==='left'){
		position=new mono.Vec3(-size.x/2, 0, 0);
		var axis=new mono.Vec3(0,1,0);
	}
	if(anchor==='right'){
		position=new mono.Vec3(size.x/2, 0, 0);
		var axis=new mono.Vec3(0,1,0);
	}

	var animation=new twaver.Animate({
		from: from,
		to: to,
		dur: 1800,
		easing: 'bounceOut',
		onUpdate: function (value) {					
			if(this.lastValue===undefined){
				this.lastValue=0;
			}
			object.rotateFromAxis(axis.clone(), position.clone(), Math.PI/180*angle*(value-this.lastValue));
			this.lastValue=value;
		},
		onDone: function(){
			delete this.lastValue;
		},
	});
	animation.play();
}

function getRandomInt(max){
	return parseInt(Math.random()*max);
}

function createDoor(box, wall, x, y, z, animation, type){	
	var width=205, height=180, depth=26;	
	var frameEdge=10, frameBottomEdge=2;
	
	var frame=new mono.Cube(width, height, depth);
	frame.s({
		'm.color': '#B8CAD5',
		'm.ambient': '#B8CAD5',
		'm.lightmap.image': 'images/inside_lightmap.jpg',
		'm.type': 'phong',
		'm.specularStrength': 0.1,
	});
	frame.setPosition(x, frame.getHeight()/2+y, z);

	var cut=new mono.Cube(width-frameEdge, height-frameEdge/2-frameBottomEdge, depth+2);
	cut.s({
		'm.color': '#B8CAD5',
		'm.ambient': '#B8CAD5',
		'm.lightmap.image': 'images/inside_lightmap.jpg',
		'm.type': 'phong',
		'm.specularStrength': 0.1,
		'bottom.m.lightmap.image': 'images/floor.jpg',
	});
	cut.setPosition(x, cut.getHeight()/2+frameBottomEdge+y, z);
	
	var frame=new mono.ComboNode([frame, cut], ['-']);
	box.add(frame);

	if(wall){
		var cutwall=new mono.ComboNode([wall, cut],['-']);
		box.add(cutwall);
	}
	
	var left=new mono.Cube((width-frameEdge)/2-2, height-frameEdge/2-frameBottomEdge-2, 2);
	left.setPosition(x-(width-frameEdge)/4, frameBottomEdge+1+left.getHeight()/2+y, z);
	left.s({
		'm.color': 'orange',
		'm.ambient': 'orange',
		'm.type': 'phong',
		'm.transparent': true,
		'front.m.texture.image': 'images/door_left.png',					
		'back.m.texture.image': 'images/door_right.png',					
		'm.specularStrength': 100,
		'm.envmap.image': envmap,
		'm.specularmap.image': 'images/white.png',	
	});		
	if(type==='metal'){
		left.s({
			'm.color': '#A4A4A4',
			'm.ambient': '#A4A4A4',
			'm.transparent': false,
			'm.texture.image': 'images/metal.jpg',					
			'm.specularStrength': 30,
			'm.envmap.image': null,
		});	
	}
	left.setClient('animation', 'rotate.left.-90');
	if(animation==='split'){
		left.setClient('animation', 'move.-x');
	}
	left.setParent(frame);

	box.add(left);

	var right=left.clone();
	right.setPositionX(x+(width-frameEdge)/4);
	right.setRotationY(Math.PI);
	right.setParent(frame);
	right.setClient('animation', 'rotate.left.90');
	if(animation==='split'){
		right.setClient('animation', 'move.x');
	}
	box.add(right);	

	return frame;
}