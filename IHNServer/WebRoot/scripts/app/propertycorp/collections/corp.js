define([
], function() {
  
  var PropertyAsset=Backbone.Model.extend({
	  defaults:{
		  id:'',
		  name:'',
		  corp:'',
		  type:'Parking',
		  floorNum:1,
		  floors:'B1',
		  city:'ShangHai',
		  address:'',
		  phone:'',
		  longitude:0,
		  latitude:0,
		  contact:'',
		  contactPhone:'',
		  description:''
	  },
	  
	  initialize : function(){
		  //alert('PropertyAsset created '+this.id);
	  }
  });	
	
  var PropertyAssetCollection = Backbone.Collection.extend({
	  model: PropertyAsset,
  });
  
  return PropertyAssetCollection;
});