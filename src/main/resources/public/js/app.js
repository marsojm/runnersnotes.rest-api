
(function(){
    var app = angular.module('runnersNotes',[]);

    app.controller('NoteController',['$http',function($http){
        var rn = this;
        rn.success = false;
        rn.errors = [];
        rn.notes = [];
        rn.note = {};
        rn.datePattern = /^(20[0-9][0-9])-(1[0-2]|0[1-9])-([0-3][0-9])$/;
        $http.get('http://localhost:8080/notes').success(function(data){
            	rn.notes = data.items;
        });

        this.clearAlerts = function() {
            rn.errors = [];
            rn.success = false;
        };

        this.addNote = function() {
            rn.note.created = new Date(rn.note.date).getTime();
            $http.post('http://localhost:8080/notes',rn.note).
                then(function(response){
                    rn.notes.push(rn.note);
                    rn.note = {};
                    rn.success = true;
                    rn.errors = [];
                    $("#addForm").collapse('hide');
                }, function(response) {
                    rn.success = false;
                    for (var i=0; i < response.data.length; i++) {
                        rn.errors.push(response.data[i]);
                    }
                });
        };
    }]);
})();