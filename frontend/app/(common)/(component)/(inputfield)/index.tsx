import Spacer from "../(spacer)"
import styles from "./input_field.module.css"

export default function InputField({
    category,
    placeholder,
    onChange,
    isRequired = false,
    maxLength,
}: InputFieldProps) {
    return (
        <div className={styles.container}>
            <div className={styles.category_container}>
                <span>{category}</span>
                <Spacer spacing={2.5} direction="row"/>
                { isRequired ? <p>*</p> : <></> }
            </div>
            <Spacer spacing={5} direction="column"/>
            <input
            className={styles.input_container}
            placeholder={placeholder}
            onChange={(e) => onChange(e.target.value)}
            maxLength={maxLength}
            />
        </div>
    )
}

interface InputFieldProps {
    category: string
    placeholder: string
    onChange: (value: string) => void
    isRequired?: boolean
    maxLength?: number
}