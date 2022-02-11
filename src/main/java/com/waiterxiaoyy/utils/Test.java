package com.waiterxiaoyy.utils;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/1/24 15:54
 * @Version 1.0
 */
public class Test {
    public void rotate(int[] nums, int k) {
        reverse(nums, 0, nums.length - 1 - k);
        reverse(nums, nums.length - k, nums.length - 1);
        reverse(nums, 0, nums.length - 1);
    }


    public void reverse(int[] nums, int i, int j) {
        while(i < j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
        test.rotate(nums, 3);
        for(int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }
    }
}
