/*
 * Hide the standard Camunda Tasklist action for creating standalone/manual tasks.
 * Process-created user tasks are unaffected.
 */

const createTaskLabels = ['create task'];

function hasCreateTaskLabel(element) {
  const text = (element.textContent || '').trim().toLowerCase();
  const title = (element.getAttribute('title') || '').trim().toLowerCase();
  const ariaLabel = (element.getAttribute('aria-label') || '').trim().toLowerCase();

  return createTaskLabels.includes(text) ||
    createTaskLabels.includes(title) ||
    createTaskLabels.includes(ariaLabel);
}

function findActionElement(element) {
  return element.closest('a, button, [role="button"], .btn') || element;
}

function hideCreateTaskActions() {
  document.querySelectorAll('a, button, [role="button"], .btn').forEach((element) => {
    if (hasCreateTaskLabel(element)) {
      findActionElement(element).style.display = 'none';
    }
  });
}

document.addEventListener('click', (event) => {
  const action = event.target.closest('a, button, [role="button"], .btn');
  if (action && hasCreateTaskLabel(action)) {
    event.preventDefault();
    event.stopPropagation();
  }
}, true);

const observer = new MutationObserver(hideCreateTaskActions);

observer.observe(document.body, {
  childList: true,
  subtree: true
});

hideCreateTaskActions();
