const selectedStudentIds = new Set();
const selectedCourseIds = new Set();

document.querySelectorAll('#selectedStudentsHiddenInputs input[name="students"]').forEach(input => {
    selectedStudentIds.add(input.value);
});
document.querySelectorAll('#selectedCoursesHiddenInputs input[name="courses"]').forEach(input => {
    selectedCourseIds.add(input.value);
});


document.addEventListener('change', function(e) {
    if (e.target.name === 'students') {
        const id = e.target.value;
        if (e.target.checked) {
            selectedStudentIds.add(id);
        } else {
            selectedStudentIds.delete(id);
        }
    }
    if (e.target.name === 'courses') {
        const id = e.target.value;
        if (e.target.checked) {
            selectedCourseIds.add(id);
        } else {
            selectedCourseIds.delete(id);
        }
    }
});

document.getElementById('groupForm').addEventListener('submit', function() {
    updateSelectedStudentHiddenInputs();
    console.log('Students to submit:', Array.from(selectedStudentIds));
    updateSelectedCourseHiddenInputs();
    console.log('Courses to submit:', Array.from(selectedCourseIds));
});

function saveCurrentlyCheckedStudents() {
    document.querySelectorAll('input[name="students"]:checked').forEach(cb => {
        selectedStudentIds.add(cb.value);
    });
}

function updateSelectedStudentHiddenInputs() {
    const studentContainer = document.getElementById('selectedStudentsHiddenInputs');
    if (!studentContainer) {
        console.error('Element with id "selectedStudentsHiddenInputs" not found');
        return;
    }
    studentContainer.innerHTML = '';
    selectedStudentIds.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'students';
        input.value = id;
        studentContainer.appendChild(input);
    });
}

function loadStudents(page, size = 5) {
    saveCurrentlyCheckedStudents();

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

            document.querySelectorAll('input[name="students"]').forEach(cb => {
                cb.checked = selectedStudentIds.has(cb.value);
            });

            updateSelectedStudentHiddenInputs();
        })
        .catch(error => {
            console.error('Error loading students:', error);
        });
}

function saveCurrentlyCheckedCourses() {
    document.querySelectorAll('input[name="courses"]:checked').forEach(cb => {
        selectedCourseIds.add(cb.value);
    });
}

function updateSelectedCourseHiddenInputs() {
    const courseContainer = document.getElementById('selectedCoursesHiddenInputs');
    if (!courseContainer) {
        console.error('Element with id "selectedCoursesHiddenInputs" not found');
        return;
    }
    courseContainer.innerHTML = '';
    selectedCourseIds.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'courses';
        input.value = id;
        courseContainer.appendChild(input);
    });
}

function loadCourses(page, size = 5) {
    saveCurrentlyCheckedCourses();

    const form = document.getElementById('groupForm');
    const formData = new FormData(form);

    formData.append('currentPage', page);
    formData.append('size', size);
    selectedCourseIds.forEach(id => formData.append('selectedCourseIds', id));

    fetch('/groups/courseSelectCheckboxList', {
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
            document.getElementById('courses-list').innerHTML = html;

            document.querySelectorAll('input[name="courses"]').forEach(cb => {
                cb.checked = selectedCourseIds.has(cb.value);
            });

            updateSelectedCourseHiddenInputs();
        })
        .catch(error => {
            console.error('Error loading courses:', error);
        });
}

