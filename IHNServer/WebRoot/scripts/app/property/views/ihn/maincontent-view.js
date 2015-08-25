define([
  'text!templates/ihn/maincontent-template.html',
  'mono'
], function (MainContentTemplate) {

  var MainContentView = Backbone.View.extend({
      
	  box: new mono.DataBox(),
	    
      parkingSpaceContainer: [],
      parkInfo: [2, 16, 17, 24, 28, 30, 37, 39, 44, 45, 50, 64, 72, 75, 80, 81, 82, 83, 100, 101, 107, 120, 140, 143, 150],
      
	  template: _.template(MainContentTemplate),
	  
	  initialize: function () {
		  this.$el.html(this.template());
		  this.render();
		  this.draw();
		  this.setEvents();
      },
      
      render: function () {
        return this;
      },
      
      setEvents: function() {
			var that = this;
			
			this.$('#ihn-demo-park').on('click', function() {
				var psId = that.$('#ihn-demo-park-id').val();
				that.park(psId);
			});
			
			this.$('#ihn-demo-unpark').on('click', function() {
				var psId = that.$('#ihn-demo-park-id').val();
				that.unpark(psId);
			});
		},
		
		draw: function() {
			var camera = new mono.PerspectiveCamera();
      	camera.setPosition(250, 350, 900);
      	
      	var network = new mono.Network3D(this.box, camera, this.$el.find('#myCanvas')[0]);
      	var interaction = new mono.DefaultInteraction(network);
      	network.setInteractions([interaction]);
      	
      	var pointLight = new mono.PointLight(0xFFFFFF, 1.2);
      	pointLight.setPosition(0, 500, 250);
      	this.box.add(pointLight);
      	this.box.add(new mono.AmbientLight(0x888888));
      	
      	//initialize parking space
      	this.box.startBatch();
      	this.createFloor({height: 100, label: 'F1', type: 'ground'});
      	this.createFloor({height: 0, label: 'B1', type: 'park'});
      	this.createFloor({height: -100, label: 'B2', type: 'park'});
      	this.createFloor({height: -200, label: 'B3', type: 'park'});
      	
      	this.createWall(this.box);
      	this.box.endBatch();
      	
      	//park
      	for(var i in this.parkInfo) {
      		this.park(this.parkInfo[i]);
      	}
      	
      	//export 3D object
      	//var jsonSerializer = new mono.JsonSerializer(network.getDataBox());
          //var json = jsonSerializer.serialize();
          //console.log(json);
		},
		
		createFloor: function(options) {
      	var height = options.height,
      	    label = options.label,
      	    type = options.type;
      	
      	var floor = new mono.Cube(1000, 5, 500);
      	floor.setPosition(0, height, 0);
      	floor.setStyle('m.texture.image', '../images/default_texture.png').setStyle('m.type', 'phong').setStyle('m.texture.repeat', new mono.Vec2(20, 20));
      	this.box.add(floor);
      	
      	if(!type) return;
      	
      	if(type == 'ground') {
      		this.createBuilding(0, height+floor.getHeight()/2, 0);
      	} else if(type == 'park') {
      		for(var i = 0; i < 3; i++) {
          		for(var j = 0; j < 18; j++) {
          			this.createParkSpace(j*50-400, height+floor.getHeight()/2, i*200-200);
              	}
          	}
      	}
      },
      
      createWall: function() {
      	var wall=new mono.Cube(5,300,500);
			wall.setPosition(-500,-50,0);
			wall.setStyle('m.texture.image','../images/wall01_inner_3d.png').setStyle('m.type','phong').setStyle('m.texture.repeat',new mono.Vec2(20,20));
			wall.setSelectable(false);
			this.box.add(wall);
      },
      
      createBuilding: function(x, y, z) {
      	var building = new mono.Cube(800, 300, 400);
      	building.setPosition(x, y+150, z);
      	building.setStyle('m.texture.image', '../images/wall03_3d_1.png');
      	building.setStyle('m.type','phong');
      	building.setStyle('m.texture.repeat', new mono.Vec2(20, 20));
      	building.setSelectable(true);
			this.box.add(building);
			
			//add window
			//front
			for(var i = 0; i < 4; i++) {
				for(var j = 0; j < 13; j++) {
					var win = new mono.Cube(30, 40, 4);
					win.setPosition(x-360+j*60, y+40+i*74, z+200);
					win.setStyle('m.texture.image', '../images/window02_3d.png');
					win.setStyle('m.type','phong');
					win.setStyle('m.texture.repeat');
					win.setSelectable(true);
					this.box.add(win);
				}
			}
			//back
			for(var i = 0; i < 4; i++) {
				for(var j = 0; j < 13; j++) {
					var win = new mono.Cube(30, 40, 4);
					win.setPosition(x-360+j*60, y+40+i*74, z-200);
					win.setStyle('m.texture.image', '../images/window02_3d.png');
					win.setStyle('m.type','phong');
					win.setStyle('m.texture.repeat');
					win.setSelectable(true);
					this.box.add(win);
				}
			}
			
			var door = new mono.Cube();
			door.setPosition(x, y+150, z);
			door.setStyle('m.texture.image', '../images/door-sliding-left.png');
			door.setStyle('m.type','phong');
			door.setStyle('m.texture.repeat');
			door.setSelectable(true);
			this.box.add(door);
      },
      
      createParkSpace: function(x, y, z) {
      	var parkSpace = new mono.Cube(30, 2, 60);
      	parkSpace.setPosition(x, y, z);
      	parkSpace.setStyle('m.texture.image', '../images/wall01_inner_3d.png');
      	parkSpace.setStyle('m.type','phong');
      	parkSpace.setStyle('m.texture.repeat', new mono.Vec2(20, 20));
      	parkSpace.setSelectable(true);
			this.box.add(parkSpace);
			
			//record park space infomation
			var index = this.parkingSpaceContainer.length;
			this.parkingSpaceContainer[index] = {
				pId: index,
				location: {
					x: x, y: y, z: z
				},
				free: true
			};
      },
      
      park: function(pId) {
      	var ps = this.parkingSpaceContainer[pId];
      	if(!ps|| ps.free == false) return false;
      	
      	var car=new mono.Billboard();
      	car.setScale(30,20,100);
      	car.setPosition(ps.location.x+10, ps.location.y, ps.location.z+20);
      	car.setStyle('m.texture.image','../images/car.png');
      	car.setStyle('m.alignment', mono.BillboardAlignment.bottomCenter);
      	car.setStyle('m.transparent', true);
      	car.setSelectable(false);
			this.box.add(car);
      	
			//update park space info
			ps.free = false;
			ps.cId = car._id;
			
      	return true;
      },
      
      unpark: function(pId) {
      	var ps = this.parkingSpaceContainer[pId];
      	if(!ps || ps.free == true) return false;
      	
      	this.box.removeById(ps.cId);
      	
      	//update park space info
			ps.free = true;
			ps.cId = null;
			
      	return true;
      }
		
	});

  return MainContentView;
});