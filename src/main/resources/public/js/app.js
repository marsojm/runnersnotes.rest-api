
(function(){
    var app = angular.module('runnersNotes',[]);

    app.controller('NoteController',['$http',function($http){
        var rn = this;
        rn.notes = [];
        $http.get('http://localhost:8080/notes').success(function(data){
                console.log(data);
            	rn.notes = data.items;
        });

        this.addNote = function(note) {

        };
    }]);
    app.controller('AddNoteController', ['$http',function($http) {
        this.note = {};

        this.addNote = function() {
            console.log(this.note);
        };

    }]);

})();