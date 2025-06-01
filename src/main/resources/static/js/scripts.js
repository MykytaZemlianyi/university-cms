const selectedStudentIds = new Set();

document.addEventListener('change', function(e) {
    if (e.target.name === 'students') {
        const id = e.target.value;
        if (e.target.checked) {
            selectedStudentIds.add(id);
        } else {
            selectedStudentIds.delete(id);
        }
    }
});

document.getElementById('groupForm').addEventListener('submit', function() {
    const container = document.getElementById('selectedStudentsHiddenInputs');
    container.innerHTML = '';

    selectedStudentIds.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'students';
        input.value = id;
        container.appendChild(input);
    });
});

function loadStudents(page, size = 5) {
    const form = document.getElementById('groupForm');
    const formData = new FormData(form);

    formData.append('currentPage', page);
    formData.append('size', size);
    selectedStudentIds.forEach(id => formData.append('selectedStudentIds', id));

    fetch('/groups/studentSelectCheckboxList', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.text();
        })
        .then(html => {
            document.getElementById('students-list').innerHTML = html;

            // Re-check previously selected students
            document.querySelectorAll(`input[name="students"]`).forEach(cb => {
                if (selectedStudentIds.has(cb.value)) {
                    cb.checked = true;
                }
            });
        })
        .catch(error => {
            console.error('Error loading students:', error);
        });
}
