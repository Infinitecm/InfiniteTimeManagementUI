
import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';
import * as Excel from "exceljs/dist/exceljs.min.js";
import * as fs from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class ExportExcelService {
  excelColumn: any = [];
  arr: any = []; hol: any = [];

  constructor() { }
  exportExcel(burnReports: any[] = [], burnReports2: any = []) {
    console.log('Excel service ');
    let date = new Date();
    var datePipe = new DatePipe('en-US');
    let adate = datePipe.transform(date, 'dd.MM.yyyy');
    //for headers 
    this.excelColumn = [];
    const reportsNumber = burnReports.length;
    if (reportsNumber > 0) {
      Object.keys(burnReports[0]).forEach((i, v) => {
        const val = i;
        if (val === 'hours') {
          const days = burnReports[0]['hours'].length;
          const daysArr = burnReports[0]['hours'];
          for (let day = 0; day < days; ++day) {
            this.excelColumn.push(`${daysArr[day]['day']} - ${daysArr[day]['date']}`);
          }
        }
        else if (val === 'weeks') {
          const weeks = burnReports[0]['weeks'].length;
          for (let week = 0; week < weeks; ++week) {
            this.excelColumn.push(`Week${week + 1}`);
          }
        }
        else {
          this.excelColumn.push(i);
        }
      });
    }
    var borderStyles = {
      top: { style: "thin" }, left: { style: "thin" },
      bottom: { style: "thin" }, right: { style: "thin" }
    };
    //workbook
    let workBook = new Excel.Workbook();
    let workSheet = workBook.addWorksheet('Instructions', { views: [{ showGridLines: false }] });
    const welcome = workSheet.getCell("C3");
    welcome.value = 'Welcome to Timesheet Template';
    workSheet.getCell('C3').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    workSheet.getCell("C3").font = { name: 'Times New Roman', underline: 'double', family: 4, bold: true, size: 12, color: { argb: 'FFFFFF' } };
    const caution = workSheet.getCell("C4");
    caution.value = "Caution:";
    const c4 = workSheet.getCell("C4");
    const how = workSheet.getCell("C5");
    how.value = "How to use it";
    const cell = workSheet.getCell('D7');
    cell.value = "Timesheet:";
    workSheet.getCell('D7').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    workSheet.getCell("D7").font = { name: 'Times New Roman', family: 4, bold: true, size: 12, fgColor: { argb: '000000' } };
    workSheet.getCell('D7').border = {
      top: { style: 'thin' },
      left: { style: 'thin' },
      bottom: { style: 'thin' },
      right: { style: 'thin' }
    };
    const cell2 = workSheet.getCell("E9");
    cell2.value = "By changing the Year and the Month the weekend dates is updated automatically from AP to AT column and the hours are calculated accordingly";
    const num1 = workSheet.getCell("D9");
    num1.value = "1";
    const cell3 = workSheet.getCell("E10");
    cell3.value = "All Columns that accept user input have NOT been colour coded";
    const num2 = workSheet.getCell("D10");
    num2.value = "2";
    const cell4 = workSheet.getCell("E11");
    cell4.value = "Holidays are colored in Green";
    workSheet.getCell('E11').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '00B140' }, bgColor: { argb: '00B140' }
    };
    const num3 = workSheet.getCell("D11");
    num3.value = "3";
    workSheet.getCell('D11').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '00B140' }, bgColor: { argb: '00B140' }
    };
    const cell5 = workSheet.getCell("E12");
    cell5.value = "Weekends are colored in Grey";
    workSheet.getCell('E12').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'A0BBB0' }, bgColor: { argb: 'A0BBB0' }
    };
    const num4 = workSheet.getCell("D12");
    num4.value = "4";
    workSheet.getCell('D12').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'A0BBB0' }, bgColor: { argb: 'A0BBB0' }
    };
    const cell6 = workSheet.getCell("E13");
    cell6.value = "Leaves are colored in Orange";
    workSheet.getCell('E13').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'FFA500' }, bgColor: { argb: 'FFA500' }
    };
    const num5 = workSheet.getCell("D13");
    num5.value = "5"; // FFA500
    workSheet.getCell('D13').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'FFA500' }, bgColor: { argb: 'FFA500' }
    };
    const cell7 = workSheet.getCell("E14");
    cell7.value = "Start and End Date gets colored Red automatically based on the Input, E.g. if the month is November and the Start date entered is December the cell is colored Red";
    workSheet.getCell('E14').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'F80707' }, bgColor: { argb: 'F80707' }
    };
    workSheet.getCell("E14").font = { argb: 'FFFFFF' };
    const num6 = workSheet.getCell("D14");
    num6.value = "6";
    workSheet.getCell('D14').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'F80707' }, bgColor: { argb: 'F80707' }
    };
    const cell8 = workSheet.getCell("E15");
    cell8.value = "Reason for End Date to be entered mandatorily";
    const num7 = workSheet.getCell("D15");
    num7.value = "7";
    const cell9 = workSheet.getCell("E16");
    cell9.value = "Employee's Billability to be entered mandatorily";
    const num8 = workSheet.getCell("D16");
    num8.value = "8";
    const cell10 = workSheet.getCell("E17");
    cell10.value = "0 Hours cannot be entered";
    const num9 = workSheet.getCell("D17");
    num9.value = "9";
    const cell11 = workSheet.getCell("E18");
    cell11.value = "If the Hours are less than the minimum working hours for the month mention the reason";
    const num10 = workSheet.getCell("D18");
    num10.value = "10";
    const cell12 = workSheet.getCell("E19");
    cell12.value = "For FP Projects leave the Bill Rate Column Blank";
    const num11 = workSheet.getCell("D19");
    num11.value = "11";
    const cell13 = workSheet.getCell("E20");
    cell13.value = "Add Rows before the ROW 22 so that the formatting and the formulas are updated in the cells automatically";
    const num12 = workSheet.getCell("D20");
    num12.value = "12";
    const cell14 = workSheet.getCell("E21");
    cell14.value = "Do not tamper the formulas which are colored in Grey";
    workSheet.getCell('E21').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'A0BBB0' }, bgColor: { argb: 'A0BBB0' }
    };
    workSheet.getCell('D21').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: 'A0BBB0' }, bgColor: { argb: 'A0BBB0' }
    };
    const num13 = workSheet.getCell("D21");
    num13.value = "13";
    for (let i = 9; i <= 21; ++i) {
      var a = 'E' + i;

      workSheet.getCell(a).alignment = { vertical: 'middle', horizontal: 'left', wrapText: true };
      workSheet.getRow(i).font = { name: 'Arial', family: 4, size: 9 }

    }

    const cell15 = workSheet.getCell("D24");
    cell15.value = "Holiday List:";
    workSheet.getCell('D24').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    workSheet.getCell('D24').border = {
      top: { style: 'thin' },
      left: { style: 'thin' },
      bottom: { style: 'thin' },
      right: { style: 'thin' }
    };
    workSheet.getCell("D24").font = { name: 'Times New Roman', family: 4, bold: true, size: 12, color: { argb: '000000' } };
    const cell16 = workSheet.getCell("E26");
    cell16.value = "To be updated in the Start of the Year";
    const num14 = workSheet.getCell("D26");
    num14.value = "1";
    const cell17 = workSheet.getCell("E27");
    cell17.value = "For Bangalore, Chennai & Hyderabad Holiday list kindly follow the list shared by HR team";
    const num15 = workSheet.getCell("D27");
    num15.value = "2";
    const cell18 = workSheet.getCell("E28");
    cell18.value = "For Onshore / Onsite Holiday kindly update as per the Client as it may vary";
    const num16 = workSheet.getCell("D28");
    num16.value = "3";
    for (let i = 2; i <= 30; ++i) {
      var l = 'B' + i;
      var r = 'G' + i;
      workSheet.getCell(l).border = { left: { style: 'thin' } };
      workSheet.getCell(r).border = { right: { style: 'thin' } };
    }
    for (let i = 66; i <= 71; ++i) {
      var t = String.fromCharCode(i) + "2";
      workSheet.getCell(t).border = { top: { style: 'thin' } };
    }
    for (let i = 66; i <= 71; ++i) {
      var b = String.fromCharCode(i) + "30";
      workSheet.getCell(b).border = { bottom: { style: 'thin' } };
    }
    for (let i = 9; i <= 21; ++i) {
      var c = "D" + i; var d = "E" + i;
      if (i === 14) {
        workSheet.getCell("E14").font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
        workSheet.getCell("D14").font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
        workSheet.getCell("D14").border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
        workSheet.getCell("D14").alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
        workSheet.getCell("E14").border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
        workSheet.getCell("E14").alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
      }
      if (i === 11) {
        workSheet.getCell("E11").font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
        workSheet.getCell("D11").font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
        workSheet.getCell("D11").border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
        workSheet.getCell("D11").alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
        workSheet.getCell("E11").border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
        workSheet.getCell("E11").alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
      }
      workSheet.getCell(c).border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
      workSheet.getCell(c).alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
      workSheet.getRow(c).font = { name: 'Arial', family: 4, size: 9 };
      workSheet.getCell(d).border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
      workSheet.getCell(d).alignment = { vertical: 'middle', horizontal: 'left', wrapText: true };
      workSheet.getRow(d).font = { name: 'Arial', family: 4, size: 9 }

    }
    for (let i = 26; i <= 28; ++i) {
      var c = "D" + i; var d = "E" + i;
      workSheet.getCell(c).border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
      workSheet.getCell(c).alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
      workSheet.getRow(c).font = { name: 'Arial', family: 4, size: 9 };
      workSheet.getCell(d).border = { left: { style: 'thin' }, right: { style: 'thin' }, top: { style: 'thin' }, bottom: { style: 'thin' } };
      workSheet.getCell(d).alignment = { vertical: 'middle', horizontal: 'left', wrapText: true };
      workSheet.getRow(d).font = { name: 'Arial', family: 4, size: 9 }
    }
    workSheet.getCell("B2").border = { left: { style: 'thin' }, top: { style: 'thin' } };
    workSheet.getCell("B30").border = { left: { style: 'thin' }, bottom: { style: 'thin' } };
    workSheet.getCell("G2").border = { right: { style: 'thin' }, top: { style: 'thin' } };
    workSheet.getCell("G30").border = { right: { style: 'thin' }, bottom: { style: 'thin' } };
    workSheet.getCell("D24").font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
    workSheet.getCell("D7").font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
    workSheet.getCell("C3").font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
    workSheet.columns.forEach(function (column1) {
      var maxLength = 0;
      column1["eachCell"]({ includeEmpty: true }, function (cell) {
        var columnLength = cell.value ? cell.value.toString().length : 5;
        if (columnLength > maxLength) {
          maxLength = columnLength;
        }
      });
      column1.width = maxLength < 5 ? 5 : maxLength;

    });
    //worksheet 2 timesheet 

    let workSheet2 = workBook.addWorksheet('TimeSheet', { views: [{ showGridLines: false }] });
    const daily = workSheet2.getCell("A1");
    daily.value = "Daily effort";
    workSheet2.getCell('A1').fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    workSheet2.getCell("A1").font = { name: 'Times New Roman', underline: 'double', family: 4, bold: true, size: 12, color: { argb: 'FFFFFF' } };

    const insheader = this.excelColumn;
    workSheet2.addRow([]);
    let headerRowInst = workSheet2.addRow(this.excelColumn);
    headerRowInst.font = { name: 'Arial', size: 9, color: { argb: 'FFFFFF' }, }
    headerRowInst.eachCell((cell, number) => {
      cell.fill = {
        type: 'pattern', pattern: 'lightTrellis',
        fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
      };
    })
    let x = burnReports.length + 1; this.arr = [];
    for (let i = 2, j = 0; i <= x && j < burnReports.length; ++i, ++j) {
      let lim = 71 + burnReports[j].hours.length;
      let k, m;
      this.arr = [];
      this.arr.push(burnReports[j].empId);
      this.arr.push(burnReports[j].empName);
      this.arr.push(burnReports[j].account);
      this.arr.push(burnReports[j].project);
      if (burnReports[j].isBillable != null) {
        this.arr.push("Billable");
      }
      else {
        this.arr.push("Not Billable");
      }
      this.arr.push(burnReports[j].location);
      for (k = 0, m = 71; k < burnReports[j].hours.length && m < lim; ++k, ++m) {

        this.arr.push(burnReports[j].hours[k].hoursPerDay);

      }
      this.arr.push(burnReports[j].totalHours);
      for (let w = 0; w < burnReports[j].weeks.length; ++w) {
        this.arr.push(burnReports[j].weeks[w]);
      }
      this.arr.push(burnReports[j].billRate);
      this.arr.push(burnReports[j].amount);
      this.arr.push(burnReports[j].noOfLeaves);
      this.arr.push(burnReports[j].noOfWorkingDays);
      this.arr.forEach(d => {
        const row = workSheet2.getRow(j + 4);
        row.values = this.arr;
        for (let i = 1; i <= this.arr.length; ++i) {
          if (i > 6) {
            row.getCell(i).alignment = { vertical: 'left', horizontal: 'left', indent: 2 };
            row.getCell(i).font = { name: 'Arial', family: 4, size: 9 };
          }

          if (row.getCell(i).value == "H") {
            row.getCell(i).fill = {
              type: 'pattern',
              pattern: 'lightTrellis', fgColor: { argb: '00B140' }, bgColor: { argb: '00B140' }
            };
            row.getCell(i).font = { name: 'Arial', family: 4, size: 9, bold: true, color: { argb: 'FFFFFF' } };
            row.getCell(i).alignment = { vertical: 'middle', horizontal: 'center' };
          } //00B140 - GREEN
          if (row.getCell(i).value == "L") {
            row.getCell(i).fill = {
              type: 'pattern',
              pattern: 'lightTrellis', fgColor: { argb: 'FFA500' }, bgColor: { argb: 'FFA500' }
            };
            row.getCell(i).font = { name: 'Arial', family: 4, size: 9, bold: true, color: { argb: 'FFFFFF' } };
            row.getCell(i).alignment = { vertical: 'middle', horizontal: 'center' };

          }
        }
      }
      );


      this.arr = [];
    }
    //main burn loop
    workSheet2.columns.forEach(function (column2) {
      var maxLength1 = 0;
      column2["eachCell"]({ includeEmpty: true }, function (cell) {
        var columnLength1 = cell.value ? cell.value.toString().length : 10;
        if (columnLength1 > maxLength1) {
          maxLength1 = columnLength1;
        }
      });
      column2.width = maxLength1 < 10 ? 10 : maxLength1;

    });
    let workSheet3 = workBook.addWorksheet('HolidayList', { views: [{ showGridLines: false }] });
    workSheet3.addRow(["Location", "Date", "Day", "Holiday"]);
    workSheet3.getCell("A1").fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    //workSheet2.getCell("A1").font = { name: 'Arial', bold: true, size: 9 ,color: { argb: 'FFFFFF' } };
    workSheet3.getCell("A1").font = { name: 'Times New Roman', family: 4, bold: true, size: 12, color: { argb: 'FFFFFF' } };
    workSheet3.getCell("B1").fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    workSheet3.getCell("B1").font = { name: 'Times New Roman', family: 4, bold: true, size: 12, color: { argb: 'FFFFFF' } };

    workSheet3.getCell("C1").fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    workSheet3.getCell("C1").font = { name: 'Times New Roman', family: 4, bold: true, size: 12, color: { argb: 'FFFFFF' } };

    workSheet3.getCell("D1").fill = {
      type: 'pattern',
      pattern: 'lightTrellis', fgColor: { argb: '1114F3' }, bgColor: { argb: '1114F3' }
    };
    workSheet3.getCell("D1").font = { name: 'Times New Roman', family: 4, bold: true, size: 12, color: { argb: 'FFFFFF' } };

    workSheet3.columns = [
      { header: 'Location', width: 30 },
      { header: 'Date', width: 15 },
      { header: 'Day', width: 10, outlineLevel: 1 },
      { header: 'Holiday', width: 30, outlineLevel: 1 }
    ];
    this.hol = [];
    for (let h = 0; h < burnReports2.length; ++h) {
      this.hol.push(burnReports2[h].location);
      this.hol.push(burnReports2[h].date);
      this.hol.push(burnReports2[h].day);
      this.hol.push(burnReports2[h].reason);
      workSheet3.addRow(this.hol);
      this.hol = [];
    } this.hol = [];
    workSheet2.eachRow({ includeEmpty: true }, function (row, rowNumber) {

      if (rowNumber > 2) {
        row.eachCell({ includeEmpty: true }, function (cell, colNumber) {
          cell.border = borderStyles;
        });
      }
    });
    const dobCol = workSheet2.getColumn(2);
    dobCol.width = 25;
    const dobCol2 = workSheet2.getColumn(4);
    dobCol2.width = 25;
    const dobCol3 = workSheet2.getColumn(5);
    dobCol3.width = 25;
    const dobCol4 = workSheet2.getColumn(3);
    dobCol4.width = 25;
    const dobCol6 = workSheet2.getColumn(1);
    dobCol6.width = 18;
    const dobCol7 = workSheet2.getColumn(6);
    dobCol7.width = 25;
    const dobCol5 = workSheet.getColumn(5);
    dobCol5.width = 130;
    workSheet.getCell("C3").font = { name: 'Arial', family: 4, size: 10, color: { argb: 'FFFFFF' } };
    workSheet.getCell("D7").font = { name: 'Arial', family: 4, size: 9, color: { argb: 'FFFFFF' } };
    workSheet.getCell("D24").font = { name: 'Arial', family: 4, size: 9, color: { argb: 'FFFFFF' } };
    workSheet2.getRow(3).font = { name: 'Arial', family: 4, size: 9, color: { argb: 'FFFFFF' } };
    workSheet3.eachRow({ includeEmpty: true }, function (row, rowNumber) {
      row.eachCell({ includeEmpty: true }, function (cell, colNumber) {
        cell.border = borderStyles;
      });
    });
    let fname = 'ReportData';
    workBook.xlsx.writeBuffer().then((data) => {
      let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      fs.saveAs(blob, fname + '-' + adate + '.xlsx');
    });

  }
  
}



 