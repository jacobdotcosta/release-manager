module.exports = {
  UpdateIssueStatus,
  EditIssue
}

var transitions = new Map()
// New to Open status (ticket displayed within the to do column of the sprint will be now able to move to In Progress)
transitions.set('HandOver', '791')
// Open to Coding In Progress
transitions.set('InProgress', '4')
// Revert to Open
transitions.set('StopProgress', '301')
// Status to close/resolve the issue
transitions.set('ResolveIssue', '5')
transitions.set('CloseIssue', '2')
transitions.set('LinkPR', '711')

async function EditIssue (client, id) {
  try {
    await client.issue.editIssue({
      issueKey: id,
      issue: {
        fields: {
          summary: 'This is a test',
          labels: [
            'bugfix',
            'blitz_test'
          ]
        }
      }
    })
  } catch (e) {
      $.Log.error(`Unable to get JIRA issue - Status code of error is:\n${e}`)
  }
}

// Change the status of the JIRA Issue to a new Transition
async function UpdateIssueStatus (client, id, transitionName) {
  try {
    client.issue.transitionIssue({
      issueKey: id,
      transition: { 'id': transitions.get(transitionName) }
    })
  } catch (e) {
      $.Log.error(`Unable to update status of the JIRA issue - Status code of error is:\n${e}`)
  }
}
