define([
], function() {
  
  var ParkElement=Backbone.Model.extend({
	  urlRoot:'/rest/park/elements',
//	  defaults:{
//		  parkid,
//		  id,
//		  name,
//		  type,
//		  category,
//		  parent,
//		  positionx,
//		  positiony,
//		  positionz,
//		  width,
//		  height,
//		  depth,
//		  picture,
//		  rotationx,
//		  rotationy,
//		  rotationz,
//		  transparent,
//		  opacity
//	  },
	  
	  initialize : function(){
	  }
  });	
	
  var ParkElementCollection = Backbone.Collection.extend({
	  model: ParkElement,
	  url: '/rest/park/elements'
  });
  
  return ParkElementCollection;
  
});