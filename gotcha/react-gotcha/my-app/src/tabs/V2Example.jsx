import { useState } from 'react'
import './Example.css'

export function V2Example() {
  const [textInputValue, setTextInputValue] = useState('');

  const [items, setItems] = useState([
    'apple',
    'banana',
  ]);

  function handleAddItem(value) {
    items.push(value)
    setItems(items);
    //1.  Will this work?
  }

  return (
    <div>
      <h1>V2 - Mutate state gotcha!</h1>
      {itemListDisplay(items)}

      {formDisplay(handleAddItem, textInputValue, setTextInputValue)}

    </div>
  )
}


export default V2Example;


function formDisplay(handleAddItem, textInputValue, setTextInputValue) {
  return <div>
    <input value={textInputValue} onChange={event => setTextInputValue(event.target.value)} />
    <button onClick={() => { handleAddItem(textInputValue) }}>Add item</button>
  </div >;
}

function itemListDisplay(items) {
  return <ul>
    {items.map((item, index) => {
      return (
        <li key={index}>
          {item}
        </li>
      );
    })}
  </ul>;
}

