import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter

def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
def searchService = ComponentAccessor.getComponent(SearchProvider)
def issueManager = ComponentAccessor.getIssueManager()
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

// query 
def query = jqlQueryParser.parseQuery("project = <Project> and issueFunction in issuesInEpics('status = Closed')")

// search
def search = searchService.search(query, user, PagerFilter.getUnlimitedFilter())

// what to do 
log.debug("Total issues: ${search.total}")
    search.getIssues().each { 
        
        documentIssue -> log.debug(documentIssue.key)

        // if you need a mutable issue you can do:
        def issue = issueManager.getIssueObject(documentIssue.id)
        if ((issue.getIssueType().name == "User Story") && (issue.status.name != "Closed" )){
            log.warn("User Story " + issue.summary + " " + issue.status.name)
        }  

}
