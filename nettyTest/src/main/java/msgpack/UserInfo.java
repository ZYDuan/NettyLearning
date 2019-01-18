package msgpack;

import org.msgpack.annotation.Message;

/**
 * @program: nettyTest
 * @author: zyd
 * @description:
 * @create: 2019-01-18 11:02
 */
@Message
public class UserInfo {
    private int Age;
    private String name;

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
