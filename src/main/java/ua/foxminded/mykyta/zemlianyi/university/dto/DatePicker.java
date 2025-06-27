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

        switch (preset) {
        case TODAY:
            this.startDate = currentDate;
            this.endDate = currentDate;
            break;
        case THIS_WEEK:
            this.startDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            this.endDate = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            break;
        case NEXT_WEEK:
            this.startDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            this.endDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                    .with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            break;
        case THIS_MONTH:
            this.startDate = currentDate.withDayOfMonth(1);
            this.endDate = currentDate.with(TemporalAdjusters.lastDayOfMonth());
            break;
        case CUSTOM:
            // Custom date range will be set in the setter methods
            break;
        default:
            break;
        }
    }

    public void setStartDate(LocalDate startDate) {
        if (preset == DatePickerPreset.CUSTOM) {
            this.startDate = startDate;
        }
    }

    public void setEndDate(LocalDate endDate) {
        if (preset == DatePickerPreset.CUSTOM) {
            this.endDate = endDate;
        }
    }

    public DatePickerPreset getPreset() {
        return preset;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}
