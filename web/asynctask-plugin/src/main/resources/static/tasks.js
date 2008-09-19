/*global blueskyminds,YAHOO*/
blueskyminds.namespace("blueskyminds.tasks", "landmine.extractor");

(function() {

    var ERROR_EVENT = "error";
    var Connect = YAHOO.util.Connect;

    var _attributes = {
        "activeTaskUrl" : "/tasks/list",
        "availableTaskUrl" : "/tasks/available",
        "refreshInterval" : 5000
    };

    function getAttribute(name) {
        return _attributes[name];
    }

    function setAttribute(name, value) {
        _attributes[name] = value;
    }

    function showIndicator() {
//        blueskyminds.events.fire("startedParsing");
    }

    function hideIndicator() {
//        blueskyminds.events.fire("stoppedParsing");
    }

    /** Callback for the list of current tasks */
    var activeTaskListCallback = {
        success: function(o) {
            hideIndicator();
            blueskyminds.dom.insertHTML("activeTaskList", o.responseText, true);
        },
        failure: function(o) {
            hideIndicator();
            blueskyminds.dom.insertHTML("activeTaskList", blueskyminds.net.errorMessage(o), true);
        },
        cache: false
    };

    /** Callback for the list of current tasks */
    var availableTaskListCallback = {
        success: function(o) {
            hideIndicator();
            blueskyminds.dom.insertHTML("availableTaskList", o.responseText, true);
        },
        failure: function(o) {
            hideIndicator();
            blueskyminds.dom.insertHTML("availableTaskList", blueskyminds.net.errorMessage(o), true);
        },
        cache: false
    };

    /**
     * @description request an update of the list of current tasks
     */
    function refreshActiveTasks() {
       Connect.resetFormState();
       showIndicator();
       Connect.asyncRequest('GET', getAttribute("activeTaskUrl"), activeTaskListCallback);
    }

    /**
     * @description request an update of the list of current tasks
     */
    function refreshAvailableTasks() {
       Connect.resetFormState();
       showIndicator();
       Connect.asyncRequest('GET', getAttribute("availableTaskUrl"), availableTaskListCallback);
    }

    function init() {
        HousePad.layout.init(null);
        var el = document.getElementById("activeTaskList");
        if (el) {
            // create the task list if defined
            setInterval(refreshActiveTasks, getAttribute("refreshInterval"));
        }
        el = document.getElementById("availableTaskList");
        if (el) {
            // create the task list if defined
            setInterval(refreshAvailableTasks, getAttribute("refreshInterval"));
        }
    }

    YAHOO.util.Event.onDOMReady(init);

    /* public */
    blueskyminds.tasks.get = function(name) {
        return getAttribute(name);
    };

    blueskyminds.tasks.set = function(name, value) {
        setAttribute(name, value);
    };

})();
