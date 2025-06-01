const selectedStudentIds = new Set();
const selectedCourseIds = new Set();

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
    // Clear previous hidden inputs for students
    const studentContainer = document.getElementById('selectedStudentsHiddenInputs');
    studentContainer.innerHTML = '';
    selectedStudentIds.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'students';
        input.value = id;
        studentContainer.appendChild(input);
    });
    // Clear previous hidden inputs for courses
    const courseContainer = document.getElementById('selectedCoursesHiddenInputs');
    courseContainer.innerHTML = '';
    selectedCourseIds.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'courses';
        input.value = id;
        courseContainer.appendChild(input);
    });

    console.log('Students to submit:', Array.from(selectedStudentIds));
    console.log('Courses to submit:', Array.from(selectedCourseIds));
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

function loadCourses(page, size = 5) {
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

            document.querySelectorAll(`input[name="courses"]`).forEach(cb => {
                if (selectedCourseIds.has(cb.value)) {
                    cb.checked = true;
                }
            });
        })
        .catch(error => {
            console.error('Error loading courses:', error);
        });
}
