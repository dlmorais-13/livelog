var livelog = angular.module('livelog', ['ngAnimate', 'ui.bootstrap', 'ngMaterial']);

// Service for the log.
livelog.factory('ll.api', [
    '$http', '$window', '$timeout',
	function($http, $window, $timeout) {
    	var service = {
    		baseUrl: $window.location.pathname,

    		// Controls if an async call to poll the log is in progress.
    		polling: true,
    		// Timeout to poll log file again.
    		pollingTimeout: false,
    		
    		// List of log files available.
    		files: [],
    		// Name of the selected log file.
    		selected: false,
    		// Last line returned in the last poll.
    		lastLine: undefined,
    		// Current file log content.
    		log: [],
    		
    		// List the available log files.
    		listFiles: function() {
    			$http({ 
    				method: 'GET', 
    				url: service.baseUrl + 'api/list-files' 
    			}).then(function(response) {
    				service.files = response.data;
    			});
    		},
    		
    		// Open a log file.
    		openFile: function(file) {
    			// TODO dlmorais - 13 de jun de 2016 - bug if a new file is opened and there is a live polling.
    			
    			// If there is a timeout active, cancels it.
    			!service.pollingTimeout || $timeout.cancel(service.pollingTimeout);
    			
    			// Reset all control variables.
    			service.lastLine = undefined;
    			service.log = [];
    			service.selected = file;
    			service.tail();
    		},
    		
    		// Returns an URL to download the log file.
    		download: function() {
    			return service.baseUrl + 'api/download?f=' + service.selected;
    		},
    		
    		// Tails the log file.
    		tail: function() {
    			// Prevent double polling.
    			if (service.polling) {
    				$http({ 
    					method: 'GET', 
    					url: service.baseUrl + 'api/tail',
    					params: {
    						f: service.selected,
    						l: _.isUndefined(service.lastLine) ? undefined : (service.lastLine + 1) 
    					}
    				}).then(function(response) {
    					// Pushes all lines to the log list.
    					Array.prototype.push.apply(service.log, response.data);
    					// Redefines the last line.
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

// Main controller for the APP.
livelog.controller('LivelogCtrl', [
	'll.api', '$rootScope', '$scope', '$interval', 
	function(llapi, $rootScope, $scope, $interval) {
		$scope.llapi = llapi;
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
