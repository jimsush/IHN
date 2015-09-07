define([], function() {
	
	var LoginModel = Backbone.Model.extend({
		
		url: '',
		
		defaults: {
			username: null,
			password: null,
			remember: false
		},
		
		initialize: function() {
			this.bind('change:remember', function() {
				
			});
		},
		
		validate: function(attributes) {
			
		}
		
	});
	
	return LoginModel;
	
});