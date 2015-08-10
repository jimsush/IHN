define([
  'text!templates/ihn/demo/demo-template.html',
  'mono'
], function(DemoTemplate) {
	
	var DemoView = Backbone.View.extend({
		
		template: _.template(DemoTemplate),
		
		initialize: function() {
			this.$el.html(this.template());
			this.draw();
			this.render();
		},
		
		render: function() {
			return this;
		},
		
		draw: function() {
			//DataBox: a container of 3D models
			var box = new mono.DataBox();
			
			//Network: show views
			var camera = new mono.PerspectiveCamera();
			camera.setPosition(50, 80, 100);
			var network = new mono.Network3D(box, camera, this.$el.find('#myCanvas')[0]);
			
			var interaction = new mono.DefaultInteraction(network);
			interaction.zoomSpeed = 30;
			network.setInteractions([interaction]);
			
			//Light
			//create point light.
			var pointLight = new mono.PointLight(0xFFFFFF,1);
			pointLight.setPosition(10000,10000,10000);
			box.add(pointLight);

			//create ambient light.
			box.add(new mono.AmbientLight(0x888888));
			
			//Add 3D object
			var node=new mono.Cube(25,25,25);
			node.setPosition(55, 50, 55);
			node.setStyle('m.texture.image','images/fence.png').setStyle('m.type','phong');
			box.add(node);
		}
		
	});
	
	return DemoView;
	
});