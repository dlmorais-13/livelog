var livelog = angular.module('livelog', ['ngAnimate', 'ui.bootstrap', 'ngMaterial']);

livelog.factory('ll.api', [
    '$http', '$window', '$timeout',
	function($http, $window, $timeout) {
    	var service = {
    		baseUrl: $window.location.pathname,

    		polling: true,
    		pollingTimeout: false,
    		
    		files: [],
    		selected: false,
    		lastLine: undefined,
    		log: [],
    		
    		listFiles: function() {
    			$http({ 
    				method: 'GET', 
    				url: service.baseUrl + 'api/list-files' 
    			}).then(function(response) {
    				service.files = response.data;
    			});
    		},
    		
    		openFile: function(file) {
    			!service.pollingTimeout || $timeout.cancel(service.pollingTimeout);
    			service.lastLine = undefined;
    			service.log = [];
    			service.selected = file;
    			service.tail();
    		},
    		
    		tail: function() {
    			if (service.polling) {
    				$http({ 
    					method: 'GET', 
    					url: service.baseUrl + 'api/tail',
    					params: {
    						f: service.selected,
    						l: _.isUndefined(service.lastLine) ? undefined : (service.lastLine + 1) 
    					}
    				}).then(function(response) {
    					Array.prototype.push.apply(service.log, response.data);
    					service.lastLine = response.data.length ? _.last(response.data).line : service.lastLine; 
    					service.pollingTimeout = $timeout(service.tail, 1000);
    				}, function() {
    					service.pollingTimeout = $timeout(service.tail, 1000);
    				});
    			} else {
    				service.pollingTimeout = $timeout(service.tail, 1000);
    			}
    		}
    	};
    	
    	return service;
	}
]);

livelog.run([
	'll.api', '$rootScope', '$interval',
	function(llapi, $rootScope, $interval) {
		$rootScope.llapi = llapi;
		llapi.listFiles();
		
		$rootScope.scrollLock = false;
		$interval(function() {
			$rootScope.scrollLock || $('.livelog-content #mdVerticalContainer .md-virtual-repeat-scroller').animate({ scrollTop: Number.MAX_VALUE }, 0);
		}, 100);
		
		$rootScope.keypress = function(e) {
			var key = e.which || e.keyCode;
			if (key == 145) {
				$rootScope.scrollLock = !$rootScope.scrollLock;
			}
		};
	}
]);