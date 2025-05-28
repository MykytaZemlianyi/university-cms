function loadStudents(page, size = 5) {
    fetch('/groups/studentSelectCheckboxList?currentPage=' + page + '&size=' + size)
        .then(response => response.text())
        .then(html => {
            document.getElementById('students-list').innerHTML = html;
            restoreChecked('students');
        });
}

function loadCourses(page) {
    fetch('/groups/coursesSelectCheckboxList?coursePage=' + page)
        .then(response => response.text())
        .then(html => {
            document.getElementById('courses-list').innerHTML = html;
            restoreChecked('courses');
        });
}

// Store checked values before AJAX update and restore after
let checkedStudents = new Set();
let checkedCourses = new Set();

function storeChecked(type) {
    if (type === 'students') {
        checkedStudents = new Set(Array.from(document.querySelectorAll('input[name="students"]:checked')).map(cb => cb.value));
    } else {
        checkedCourses = new Set(Array.from(document.querySelectorAll('input[name="courses"]:checked')).map(cb => cb.value));
    }
}

function restoreChecked(type) {
    if (type === 'students') {
        checkedStudents.forEach(id => {
            let cb = document.querySelector('input[name="students"][value="' + id + '"]');
            if (cb) cb.checked = true;
        });
    } else {
        checkedCourses.forEach(id => {
            let cb = document.querySelector('input[name="courses"][value="' + id + '"]');
            if (cb) cb.checked = true;
        });
    }
}