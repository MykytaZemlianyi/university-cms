package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.springframework.format.annotation.DateTimeFormat;

public class DatePicker {
    private DatePickerPreset preset;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private LocalDate currentDate = LocalDate.now();

    public void setPreset(DatePickerPreset preset) {
        this.preset = preset;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;

    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;

    }

    public DatePickerPreset getPreset() {
        return preset;
    }

    public LocalDate getStartDate() {
        switch (preset) {
        case TODAY:
            return currentDate;
        case THIS_WEEK:
            return currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        case NEXT_WEEK:
            return currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        case THIS_MONTH:
            return currentDate.withDayOfMonth(1);
        case CUSTOM:
            return startDate;
        default:
            return startDate;
        }
    }

    public LocalDate getEndDate() {
        switch (preset) {
        case TODAY:
            return currentDate;
        case THIS_WEEK:
            return currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        case NEXT_WEEK:
            return currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                    .with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        case THIS_MONTH:
            return currentDate.with(TemporalAdjusters.lastDayOfMonth());
        case CUSTOM:
            return endDate;
        default:
            return endDate;
        }
    }

}
