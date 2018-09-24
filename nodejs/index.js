/**
 * How to use the Node JS Jira client
 * npm install
 * node index.js -u JIRA_USER -p JIRA_PWD -k SB-Num
 */

let operation = require('./lib/operations.js')
let argv = require('minimist')(process.argv.slice(2))

// Instantiate JIRA Client using command line parameters
function init () {
  var host
  if (argv.h == null) {
    host = 'jira.jboss.org'
  } else {
    host = argv.h
  }
  operation.newClient(host, argv.u, argv.p)
}

init()

switch (argv.o) {
    case "get":
        // Call Get Issue
        if (argv.k) {
            operation.get.IssueById({ issueKey: argv.k })
        } else {
            operation.get.IssueById({ issueKey: 'SB-889' })
        }
        break
    case "update":
        operation.update.Status({ issueKey: argv.k })
}
