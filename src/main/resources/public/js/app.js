
(function(){
    var app = angular.module('runnersNotes',[]);

    app.controller('NoteController',['$http',function($http){
        var rn = this;
        rn.success = false;
        rn.errors = [];
        rn.notes = [];
        rn.note = {};
        $http.get('http://localhost:8080/notes').success(function(data){
            	rn.notes = data.items;
        });

        this.addNote = function() {
            rn.note.created = new Date(rn.note.date).getTime();
            $http.post('http://localhost:8080/notes',rn.note).
                then(function(response){
                    //if (response.status === 201) {
                        rn.notes.push(rn.note);
                        rn.note = {};
                        rn.success = true;
                        rn.errors = [];
                        $("#addForm").collapse('hide');
                    //}
                }, function(response) {
                    rn.success = false;
                    for (var i=0; i < response.data.length; i++) {
                        rn.errors.push(response.data[i]);
                    }
                    console.log(response);
                });
        };
    }]);
})();