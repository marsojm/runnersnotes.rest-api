
(function(){
    var app = angular.module('runnersNotes',[]);

    app.controller('NoteController',['$http',function($http){
        var rn = this;
        rn.notes = [];
        $http.get('http://localhost:8080/notes').success(function(data){
                console.log(data);
            	rn.notes = data.items;
        });
    }]);
})();