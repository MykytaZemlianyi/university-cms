function loadStudents(page, size = 5) {
    const form = document.getElementById('groupForm');
    const formData = new FormData(form);

    formData.append('currentPage', page);
    formData.append('size', size);

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
        })
        .catch(error => {
            console.error('Error loading students:', error);
        });
}