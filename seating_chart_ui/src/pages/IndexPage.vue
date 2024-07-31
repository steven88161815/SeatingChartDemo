<template>
  <q-page class="q-pa-md">
    <!-- 下拉選單選擇員工 -->
    <div class="dropdown-container">
      <q-select
        v-model="selectedEmployeeId"
        :options="employeeOptions"
        label="選擇員工"
        emit-value
        map-options
        filled
      />
    </div>

    <!-- 假設座位都是 m*n 配置 -->
    <div class="panel-container">
      <div v-for="row in numRows" :key="'row-' + row" class="panel-row">
        <div
          v-for="col in numCols"
          :key="'col-' + col"
          class="panel"
          @click="selectSeat(row - 1, col - 1)"
          :class="{
            selected: selectedSeatIndex === (row - 1) * numCols + (col - 1),
          }"
          :style="{
            backgroundColor: colors[(row - 1) * numCols + (col - 1)],
          }"
        >
          {{ getPanelText(row - 1, col - 1) }}
        </div>
      </div>
    </div>

    <!-- 顏色說明 -->
    <div class="color-legend">
      <div class="color-box grey"></div>
      <span>：未選擇</span>
    </div>
    <div class="color-legend">
      <div class="color-box lightblue"></div>
      <span>：已選擇</span>
    </div>
    <div class="color-legend">
      <div class="color-box red"></div>
      <span>：已佔用</span>
    </div>

    <!-- 送出按鈕 -->
    <div class="submit-container">
      <q-btn label="送出" @click="submitSeatSelection" color="primary" />
    </div>
  </q-page>
</template>

<script setup>
import { ref, watch } from "vue";
import axios from "axios";

// 設定面板網格的初始行數和列數
const numRows = ref(4);
const numCols = ref(5);

// 初始化顏色和員工數據
const colors = ref(Array(numRows.value * numCols.value).fill("#ccc"));
const employeeData = ref([]);
const employeeOptions = ref([]);
const selectedEmployeeId = ref(null);
const selectedSeatIndex = ref(null); // 記錄選中的座位索引

// 切換顏色函數
const selectSeat = (row, col) => {
  const index = row * numCols.value + col;

  // 如果選擇的座位是當前已選擇的座位，則切換回未選擇狀態
  if (selectedSeatIndex.value === index) {
    // 如果選中的座位已經是紅色，則不改變顏色
    if (colors.value[index] === "red") {
      return;
    }
    colors.value[index] =
      colors.value[index] === "lightblue" ? "#ccc" : "lightblue";
    selectedSeatIndex.value = null;
  } else {
    // 將先前選擇的座位顏色恢復為白色（如果之前有選擇的座位）
    if (
      selectedSeatIndex.value !== null &&
      colors.value[selectedSeatIndex.value] !== "red"
    ) {
      colors.value[selectedSeatIndex.value] = "#ccc";
    }

    // 如果當前選擇的座位已經被佔用（紅色），則不改變顏色
    if (colors.value[index] === "red") {
      selectedSeatIndex.value = index;
      return;
    }

    // 將當前選擇的座位顏色設置為淺藍色
    colors.value[index] = "lightblue";
    selectedSeatIndex.value = index;
  }
};

// 根據員工數據和座位位置獲取面板顯示的字
const getPanelText = (row, col) => {
  const index = row * numCols.value + col;
  const employee = employeeData.value.find(
    (emp) => emp.floorSeatSeq === index + 1
  );
  return employee
    ? `${row + 1} 樓：座位 ${col + 1} [員編${employee.empId}]`
    : `${row + 1} 樓：座位 ${col + 1}`;
};

// 送出選擇的座位和員工資料
const submitSeatSelection = async () => {
  if (!selectedEmployeeId.value) {
    alert("請選擇一位員工");
    return;
  }

  // 確保選擇的座位存在
  if (selectedSeatIndex.value === null) {
    alert("請選擇一個座位");
    return;
  }

  const seatPosition = selectedSeatIndex.value + 1; // 轉換為 1 索引

  try {
    // 發送更新請求
    await axios.post(
      `http://localhost:8081/employees/updateSeat/${selectedEmployeeId.value}/${seatPosition}`,
      {}
    );

    // 成功後重新加載數據
    await fetchData();
  } catch (error) {
    console.error("Error updating seat:", error);
    alert("更新座位時出錯");
  }
};

const fetchData = async () => {
  try {
    // 獲取座位數據
    const seatingResponse = await axios.get(
      "http://localhost:8081/seating/all"
    );
    const seatingData = seatingResponse.data;
    numRows.value = seatingData.floorNo;
    numCols.value = seatingData.seatNo;
    // 更新顏色以匹配新的網格大小
    colors.value = Array(numRows.value * numCols.value).fill("#ccc");

    // 獲取員工數據
    const employeeResponse = await axios.get(
      "http://localhost:8081/employees/all"
    );
    employeeData.value = employeeResponse.data;

    // 生成下拉選單的選項
    employeeOptions.value = employeeData.value.map((emp) => ({
      label: `${emp.name} (${emp.empId})`,
      value: emp.empId,
    }));

    // 根據員工資料更新顏色
    updatePanelColors();
    console.log(seatingResponse);
    console.log(employeeResponse);
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

// 更新面板顏色
const updatePanelColors = () => {
  colors.value = Array(numRows.value * numCols.value).fill("#ccc"); // 重新初始化顏色
  employeeData.value.forEach((emp) => {
    if (emp.floorSeatSeq) {
      const index = emp.floorSeatSeq - 1; // 轉換為 0 索引
      if (index >= 0 && index < colors.value.length) {
        colors.value[index] = "red"; // 設置顏色為紅色
      }
    }
  });
};

// 初始化方法
const init = async () => {
  await fetchData();
};

// 呼叫初始化方法
init();

// 監聽 numRows 和 numCols 的變化
watch(
  [numRows, numCols],
  () => {
    colors.value = Array(numRows.value * numCols.value).fill("#ccc");
  },
  { immediate: true }
);
</script>

<style scoped>
.panel-container {
  display: flex;
  flex-direction: column;
}

.panel-row {
  display: flex;
}

.panel {
  flex: 1;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  cursor: pointer;
  border: 1px solid #ccc;
  box-sizing: border-box;
  margin: 2px; /* 調整面板之間的間距，防止擠壓 */
}

.panel.selected {
  border-color: #000; /* 選中座位的邊框顏色 */
  border-width: 2px;
  border-style: solid;
}

.dropdown-container {
  margin-bottom: 16px;
}

.submit-container {
  margin-top: 16px;
}

/* 畫面底部顏色說明樣式 */
.color-legend {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.color-box {
  width: 20px;
  height: 20px;
  margin-right: 10px;
  border: 1px solid #ccc;
}

.lightblue {
  background-color: lightblue;
}

.red {
  background-color: red;
}

.grey {
  background-color: #ccc;
  color: #333; /* 調整文字顏色為深灰色 */
  margin-right: 10px;
}
</style>
